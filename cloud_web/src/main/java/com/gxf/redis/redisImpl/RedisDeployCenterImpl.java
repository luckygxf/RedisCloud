package com.gxf.redis.redisImpl;

import com.gxf.common.util.ConstUtil;
import com.gxf.common.util.IdempotentConfirmer;
import com.gxf.communication.AgentCommunication;
import com.gxf.constant.EmptyObjectConst;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.InstanceInfo;
import com.gxf.protocol.Protocol;
import com.gxf.protocol.RedisProtocol;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.redis.util.RedisClusterNode;
import com.gxf.test.Test;
import com.gxf.util.*;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.exceptions.JedisDataException;
import sun.management.AgentConfigurationError;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarException;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisDeployCenterImpl implements RedisDeployCenter {
    private static Logger logger = LoggerFactory.getLogger(RedisDeployCenterImpl.class);
    @Autowired
    private static RedisCenter redisCenter;
    @Autowired
    private InstanceInfoDao instanceInfoDao;

    @Override
    public boolean deploySentinelInstance(long appId, String masterHost, String[] slaveHosts, int maxMemory, String[] sentinelIps, int appPort, List<WebJedis> jedisList) {
        String redisConfigFileName = RedisProtocol.getConfigFileName(appPort, ConstUtil.CACHE_REDIS_STANDALONE);
//        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig();

        return true;
    }

    @Override
    public boolean deployRedisInstance(String host, int port, int type) {
        //redis-server %s &
        String shellTemplate = "redis-server %s &";
        String machinePath = RedisProtocol.getMachinePath(port, type);
        String configName = RedisProtocol.getConfigFileName(port, type);
//        String runShell = String.format(shellTemplate, machinePath + configName);
        String runShell = RedisProtocol.getRunShell(port, type);
        String password = PasswordUtil.genRandomNum(16);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig(port, password);

        boolean isSuccess = AgentCommunication.runInstance(host, port, type, password, configName, redisConfigs, runShell, machinePath);
        if(isSuccess){
            logger.info("redis deploy success");
        }else{
            logger.info("redis deploy fail");
            return false;
        }

        //新建etcd节点
        EtcdUtil.createInstanceNode(host, port);

        //保存到数据库
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setHost(host);
        instanceInfo.setPort(port);
        instanceInfo.setPassword(password);
        instanceInfoDao.addInstanceInfo(instanceInfo);

        return isSuccess;

    }

    /**
     * 部署哨兵模式
     * */
    @Override
    public boolean deploySentinelModel(String masterHost, String[] slaveHosts,int type, String[] sentinelIps, int masterPort, int slavePort, int[] sentinelPorts, List<WebJedis> jedisList) {
        //1. 启动master实例
        String configFileName = RedisProtocol.getConfigFileName(masterPort, type);
        String password = PasswordUtil.genRandomNum(16);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig(masterPort, password);
        String masterauth = "masterauth %s";
        masterauth = String.format(masterauth, password);
        redisConfigs.add(masterauth);

        String runShell = RedisProtocol.getRunShell(masterPort, type);
        String machinePath = RedisProtocol.getMachinePath(masterPort, type);
        boolean isPortUsed = AgentCommunication.isPortUsed(masterHost, masterPort);
        if (isPortUsed) {
            logger.error("port:{} is already run in the host:{}", masterPort, masterHost);
            return false;
        }
        boolean isMasterRun = AgentCommunication.runInstance(masterHost, masterPort,type, password, configFileName, redisConfigs, runShell, machinePath);
        if(!isMasterRun){
            logger.error("runInstance, masterHost:{}, masterPort:{} is failed.", masterHost, masterPort);
            return false;
        }
        WebJedis webJedis = new WebJedis(masterHost, masterPort);
        try{
            logger.info("password = {}", password);
            webJedis.auth(password);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }
        jedisList.add(webJedis);
        logger.info("runInstance, masterHost:{}, masterPort:{} success", masterHost, masterPort);

        //1.2 启动slave实例
        String slaveHost = slaveHosts[0];
        redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig(slavePort, password);
        runShell = RedisProtocol.getRunShell(slavePort, type);
        machinePath = RedisProtocol.getMachinePath(slavePort, type);
        configFileName = RedisProtocol.getConfigFileName(slavePort, type);

        masterauth = String.format(masterauth, password);
        redisConfigs.add(masterauth);

        isPortUsed = AgentCommunication.isPortUsed(slaveHost, slavePort);
        if(isPortUsed){
            logger.error("port:{} is already used in the host:{}", slavePort, slaveHost);
            return false;
        }
        boolean isSlaveRun = AgentCommunication.runInstance(slaveHost, slavePort, type, password, configFileName, redisConfigs, runShell, machinePath);
        if(!isSlaveRun){
            logger.error("runInstance, slaveHost:{}, slavePort, failed.", slaveHost, slavePort);
            return false;
        }
        webJedis = new WebJedis(slaveHost, slavePort);
        try{
            webJedis.auth(password);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }
        jedisList.add(webJedis);
        logger.info("runInstance, slaveHost:{}, slavePort:{} success", slaveHost, slavePort);

        //1.3 添加slaveof配置
        boolean slaveOfSuccess = slaveOf(masterHost, masterPort, slaveHost, slavePort, password);
        if(!slaveOfSuccess){
            logger.error("slaveof masterHost:{}, masterPort:{}, slaveHost:{}, slavePort:{} failed", masterHost, masterPort
                            , slaveHost, slavePort);
            return false;
        }
        logger.info("slaveof masterHost:{}, masterPort:{}, slaveHost:{}, slavePort:{} success", masterHost, masterPort
                , slaveHost, slavePort);

        //1.4 运行sentinel group
        boolean isSentinelRun = runSentinelGroup(sentinelIps[0], sentinelPorts, masterHost, masterPort, password, jedisList);
        if(!isSentinelRun){
            logger.error("runSentinelGroup failed. sentileIp:{}, sentinel ports:{}", sentinelIps[0], sentinelPorts);
            return false;
        }
        logger.info("runSentinelGroup success. sentileIp:{}, sentinel ports:{}", sentinelIps[0], sentinelPorts);
        return true;
    }

    /**
     * 部署集群
     * */
    @Override
    public boolean deployCluster(String masterHost, String slaveHost, int[] masterPorts, int[] slavePorts) {
        List<WebJedis> clusterJedis = new ArrayList<WebJedis>();

        List<RedisClusterNode> clusterNodes = new ArrayList<RedisClusterNode>();
        //这里共有3个node
        //数组内容顺序master slave master slave master slave
        for(int i = 0; i < masterPorts.length; i++){
            RedisClusterNode node = new RedisClusterNode(masterHost, masterPorts[i], slaveHost, slavePorts[i]);
            clusterNodes.add(node);
            clusterJedis.add(new WebJedis(masterHost, masterPorts[i]));
            clusterJedis.add(new WebJedis(slaveHost, slavePorts[i]));
        }
        //开始部署集群
        boolean result = deployClusterInstance(clusterNodes);

        if(!result){
            logger.error("deploy cluster instance failed rollback");
            clusterDeployFaileRollback(clusterJedis);
        }
        return result;
    }

    /**
     * 根据节点部署集群
     * */
    @Override
    public boolean deployClusterInstance(List<RedisClusterNode> nodes) {
        String confFileNames[] = new String[nodes.size() * 2];
        //port --> redisConfig
        Map<Integer, List<String>> mapOfRedisConfigs = new HashMap<Integer, List<String>>();
        String runShells[] = new String[nodes.size() * 2];
        String machinePaths[] = new String[nodes.size() *2];

        Map<WebJedis, WebJedis> clusterMap = new LinkedHashMap<WebJedis, WebJedis>();
        //这里node有3个 master slave master slave master slave
        for(int i = 0; i < nodes.size(); i++){
            //master
            String confFileName = RedisProtocol.getConfigFileName(nodes.get(i).getMasterPort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            confFileNames[i] = confFileName;
            List<String> redisConfigs = RedisConfigUtil.getMinRedisClusterInstanceConfig(nodes.get(i).getMasterPort(), confFileName);
            mapOfRedisConfigs.put(nodes.get(i).getMasterPort(), redisConfigs);
            String runShell = RedisProtocol.getRunShell(nodes.get(i).getMasterPort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            runShells[i] = runShell;
            String machinePath = RedisProtocol.getMachinePath(nodes.get(i).getMasterPort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            machinePaths[i] = machinePath;

            //slave
            confFileName = RedisProtocol.getConfigFileName(nodes.get(i).getSlavePort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            confFileNames[i + 1] = confFileName;
            redisConfigs = RedisConfigUtil.getMinRedisClusterInstanceConfig(nodes.get(i).getSlavePort(), confFileName);
            mapOfRedisConfigs.put(nodes.get(i).getSlavePort(), redisConfigs);
            runShell = RedisProtocol.getRunShell(nodes.get(i).getSlavePort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            runShells[i + 1] = runShell;
            machinePath = RedisProtocol.getMachinePath(nodes.get(i).getSlavePort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
            machinePaths[i + 1] = machinePath;

            //start master instance
            boolean isPortUsed = AgentCommunication.isPortUsed(nodes.get(i).getMasterHost(), nodes.get(i).getMasterPort());
            if(isPortUsed){
                logger.error("port:{} is used in host:{}", nodes.get(i).getMasterPort(), nodes.get(i).getMasterHost());
                return false;
            }
            boolean isMasterRun = AgentCommunication.runInstance(nodes.get(i).getMasterHost(), nodes.get(i).getMasterPort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER,
                    EmptyObjectConst.EMPTY_STRING, confFileNames[i], mapOfRedisConfigs.get(nodes.get(i).getMasterPort()), runShells[i], machinePaths[i]);
            if(!isMasterRun){
                logger.error("runInstance in host:{}, port:{} failed.", nodes.get(i).getMasterHost(), nodes.get(i).getMasterPort());
                return false;
            }
            logger.info("runInstance in host:{}, port:{} success.", nodes.get(i).getMasterHost(), nodes.get(i).getMasterPort());

            //start slave instance
            isPortUsed = AgentCommunication.isPortUsed(nodes.get(i).getSlaveHost(), nodes.get(i).getSlavePort());
            if(isPortUsed){
                logger.error("port:{} is already used in host:{}", nodes.get(i).getSlavePort(), nodes.get(i).getSlaveHost());
                return false;
            }
            boolean isSlaveRun = AgentCommunication.runInstance(nodes.get(i).getSlaveHost(), nodes.get(i).getSlavePort(), ConstUtil.CACHE_TYPE_REDIS_CLUSTER,
                    EmptyObjectConst.EMPTY_STRING, confFileNames[i + 1], mapOfRedisConfigs.get(nodes.get(i).getSlavePort()), runShells[i + 1], machinePaths[i + 1]);
            if(!isSlaveRun){
                logger.error("runInstance in host:{}, port:{} failed.", nodes.get(i).getSlaveHost(), nodes.get(i).getSlavePort());
                return false;
            }
            logger.info("runInstance in host:{}, port:{} success.", nodes.get(i).getSlaveHost(), nodes.get(i).getSlavePort());
            WebJedis masterJedis = new WebJedis(nodes.get(i).getMasterHost(), nodes.get(i).getMasterPort());
            WebJedis slaveJedis = new WebJedis(nodes.get(i).getSlaveHost(), nodes.get(i).getSlavePort());
            clusterMap.put(masterJedis, slaveJedis);
        } //for

        //start cluster
        boolean isClusterSuccess = startCluster(clusterMap);
        if(!isClusterSuccess){
            logger.error("start cluster failed.");
            return false;
        }
        return true;
    }

    /**
     * 将启动的实例配置成集群
     * */
    private boolean startCluster(Map<WebJedis, WebJedis> clusterMap){
        final WebJedis jedis = new ArrayList<WebJedis>(clusterMap.keySet()).get(0);
        int port = jedis.getClient().getPort();
        //meet集群节点
        for(final WebJedis master : clusterMap.keySet()){
            boolean isMeet = new IdempotentConfirmer(){

                @Override
                public boolean execute() {
                    boolean isMeetSuccess = clusterMeet(jedis, master.getClient().getHost(), master.getClient().getPort());
                    return isMeetSuccess;
                } //execute
            }.run();

            if(!isMeet){
                logger.info("master host:{}, port:{} meet failed", master.getClient().getHost(), master.getClient().getPort());
                return false;
            }
            //meet slave
            final WebJedis slaveJedis = clusterMap.get(master);
            if (null != slaveJedis){
                isMeet = new IdempotentConfirmer(){

                    @Override
                    public boolean execute() {
                        boolean isMeetSuccess = clusterMeet(jedis, slaveJedis.getClient().getHost(), slaveJedis.getClient().getPort());
                        return isMeetSuccess;
                    } //execute
                }.run();
                if(!isMeet){
                    logger.error("slave host:{}, port:{} meet failed.", slaveJedis.getClient().getHost(), slaveJedis.getClient().getPort());
                    return false;
                }
            } //if

        } //for

        logger.info("cluster meet success!");

        //assign slots
        int masterSize = clusterMap.size();
        int perSize = (int) Math.ceil(16834 / masterSize);
        int index = 0;
        int masterIndex = 0;
        ArrayList<Integer> slots = new ArrayList<Integer>();
        List<WebJedis> masters = new ArrayList<WebJedis>(clusterMap.keySet());

        //start to assign slots
        for(int slot = 0; slot <= 16383; slot++){
            slots.add(slot);
            if(index ++ >= perSize || slot == 16383){
                final int[] slotArray = new int[slots.size()];
                for(int i = 0; i < slotArray.length; i++){
                    slotArray[i] = slots.get(i);
                } //for
                final WebJedis masterJedis = masters.get(masterIndex ++);
                boolean isSlot = new IdempotentConfirmer(){

                    @Override
                    public boolean execute() {
                        String response = masterJedis.clusterAddSlots(slotArray);
                        boolean isSlotSuccess = response != null && response.equalsIgnoreCase("OK");
                        if(!isSlotSuccess){
                            logger.error("allocate slots failed.");
                            return false;
                        }

                        return true;
                    } //execute
                }.run();
                if(!isSlot){
                    logger.error("{}:{} set slots:{} failed.", masterJedis.getClient().getHost(), masterJedis.getClient().getPort(), slotArray);
                    return false;
                }
                slots.clear();
                index = 0;
            } //if
        } //for
        logger.info("cluster allocate 16384 slot success!");

        //设置从节点
        for(WebJedis masterJedis : clusterMap.keySet()){
            final WebJedis slaveJedis = clusterMap.get(masterJedis);
            if(slaveJedis == null){
                continue;
            } //if
            final String nodeId = getClusterNodeId(masterJedis);
            boolean isReplicate = new IdempotentConfirmer(){

                @Override
                public boolean execute() {
                    try{
                        //等待广播节点
                        TimeUnit.SECONDS.sleep(2);
                    }catch (Exception e){
                        logger.error(e.getMessage(), e);
                    }
                    String response = "";
                    try{
                        response = slaveJedis.clusterReplicate(nodeId);
                    } catch (Exception e){
                        logger.error(e.getMessage(), e);
                    }
                    boolean isReplicateSucces = response != null && response.equalsIgnoreCase("OK");
                    if(!isReplicateSucces){
                        try{
                            TimeUnit.SECONDS.sleep(2);
                        }catch (Exception e){
                            logger.error(e.getMessage(), e);
                        }

                        return false;
                    }
                    return true;
                } //execute
            }.run();
            if(!isReplicate){
                logger.error("{}:{} set replicate:failed", slaveJedis.getClient().getHost(), slaveJedis.getClient().getPort());
                return false;
            }
        } //for
        logger.info("cluster set master slave replicate success");
        return true;
    }

    /**
     * 部署哨兵组
     * 这里由于在虚拟机中部署
     * ip是一个，端口有多个
     * */
    private boolean runSentinelGroup(String sentinelIp,int sentinelPorts[], String masterHost, int masterPort, String password, List<WebJedis> jedisList){
        String masterName = getMasterName(masterHost, masterPort);

        String sentinelConfNames[] = new String[sentinelPorts.length];
        for(int i = 0; i < sentinelPorts.length; i++){
            sentinelConfNames[i] = RedisProtocol.getConfigFileName(sentinelPorts[i], ConstUtil.CACHE_REDIS_SENTINEL);
        }
        //port-->configs
        Map<Integer, List<String>> sentinelConfigMap = new HashMap<Integer, List<String>>();
        for(int i = 0 ; i < sentinelPorts.length; i++){
            List<String> sentinelConfigs = SentinelConfigUtil.getSentinelConfigs(masterHost, masterPort, password, masterName, sentinelIp, sentinelPorts[i]);
            sentinelConfigMap.put(sentinelPorts[i], sentinelConfigs);
        }
        String machinePaths[] = new String[sentinelPorts.length];
        for(int i = 0; i < sentinelPorts.length; i++){
            String machinePath = RedisProtocol.getMachinePath(sentinelPorts[i], ConstUtil.CACHE_REDIS_SENTINEL);
            machinePaths[i] = machinePath;
        }
        String runShells[] = new String[sentinelPorts.length];
        for(int i = 0; i < sentinelPorts.length; i++){
            String runShell = RedisProtocol.getRunShell(sentinelPorts[i], ConstUtil.CACHE_REDIS_SENTINEL);
            runShells[i] = runShell;
        }
        //开始部署sentinel实例
        boolean isPortUsed = false;
        for(int i = 0; i < sentinelPorts.length; i++){
            isPortUsed = AgentCommunication.isPortUsed(sentinelIp, sentinelPorts[i]);
            if(isPortUsed){
                logger.error("port:{} is already used in the host:{}", sentinelPorts[i], sentinelIp);
                return false;
            } //if

            boolean isRun = AgentCommunication.runSentinel(sentinelIp, sentinelPorts[i], sentinelConfNames[i], sentinelConfigMap.get(sentinelPorts[i]),
                                                            runShells[i], machinePaths[i]);
            if(!isRun){
                logger.error("runSentinel, sentienlPort:{}, masterName:{}, configFileName:{} failed.", sentinelPorts[i], masterName, sentinelConfNames[i]);
                return false;
            }
            WebJedis webJedis = new WebJedis(sentinelIp, sentinelPorts[i]);
            jedisList.add(webJedis);
        } //for

        return true;
    }

    /**
     * 生成mastername
     * */
    private String getMasterName(String host, int port){
        String masterSentinelName = String.format("sentinel-%S-%s", host, port);
        return masterSentinelName;
    }

    /**
     * 设置主从关系
     * */
    private boolean slaveOf(final String masterHost, final int masterPort, String slaveHost,
                            int slavePort, String password){
        final WebJedis slaveJedis = new WebJedis(slaveHost, slavePort, Protocol.DEFAULT_TIMEOUT * 2);
        try{
            slaveJedis.auth(password);
        } catch (Exception e){
            logger.error("auth slave node error, slavehost is:{}, slaveport is:{}", slaveHost, slavePort);
            logger.error(e.getMessage(), e);
            return false;
        }

        try{
            boolean isSlaveSuccess = new IdempotentConfirmer(){

                @Override
                public boolean execute() {
                    String result = slaveJedis.slaveof(masterHost, masterPort);
                    return null != result && result.equalsIgnoreCase("OK");
                }
            }.run();
            if(!isSlaveSuccess){
                logger.error("slavehost:{}, slaveport:{}, slaveof failed, masterhost:{}, masterport:{}", slaveHost, slavePort, masterHost, masterPort);
                return false;
            }
            redisCenter.configRewreite(slaveHost, slavePort, password);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if(slaveJedis != null){
                slaveJedis.close();
            }
        }

        return true;
    }

    /**
     * meet集群中的节点
     * */
    private boolean clusterMeet(WebJedis webJedis, String host, int port){
        boolean isSingleNode = redisCenter.isSingleClusterNode(host, port);
        if(!isSingleNode){
            logger.error("{}:{} is not single node",host, port);
            return false;
        }
        logger.error("{}:{} is single node", host, port);

        String response = webJedis.clusterMeet(host, port);
        boolean isMeet = response != null && response.equalsIgnoreCase("OK");
        if(!isMeet){
            logger.error("{}:{} meet error", host, port);
            return false;
        }

        logger.info("{}:{} meet success", host, port);

        return true;
    }

    /**
     * 获取集群节点nodeId
     * */
    private String getClusterNodeId(WebJedis jedis){
        try{
            String infoOutput = jedis.clusterNodes();
            for(String infoLine : infoOutput.split("\n")){
                if(infoLine.contains("myself")){
                    return infoLine.split(" ")[0];
                } //if
            } //for
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }

        return "";
    }

    /**
     * 部署失败，关闭以前启动的redis实例
     * */
    private void clusterDeployFaileRollback(List<WebJedis> clusterJedis){
        for(WebJedis jedis : clusterJedis){
            try{
                jedis.shutdown();
                logger.info("shut down redis host:{}, port:{} success", jedis.getClient().getHost(), jedis.getClient().getPort());
                logger.info("deploy failed, roll back shutdown redis");
            } catch (Exception e){
                logger.info("shut down redis host:{}, port:{} failed", jedis.getClient().getHost(), jedis.getClient().getPort());
                logger.error(e.getMessage(), e);
            } finally {
                if(jedis != null){
                    try{
                        jedis.close();
                    } catch (Exception e){
                        logger.error(e.getMessage(), e);
                    }
                } //if
            } //finally
        } //for

        logger.info("deplay failed, roll back all redis instance");
    }

    /**
     * 下线指定redis实例
     * */
    public boolean shutdown(String ip, int port, String password){
        boolean isRun = isRun(ip, port, password);
        if(!isRun){
            return true;
        }
        final WebJedis jedis = new WebJedis(ip, port);
        if(StringUtil.isNotEmpty(password)){
            jedis.auth(password);
        }
        try{
            //关闭实例节点
            boolean isShutdown = new IdempotentConfirmer(){
                @Override
                public boolean execute(){
                    jedis.shutdown();
                    return true;
                }
            }.run();
            if(!isShutdown){
                logger.error("host:{}, port:{} redis is not shutdown!", ip, port);
            }
            logger.info("host:{}, port:{} redis is shutdown!", ip, port);
            return isShutdown;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if(null != jedis){
                try{
                    jedis.close();
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        } //finally
    }

    /**
     * 判断是否有实例在端口运行
     * */
    public boolean isRun(final String ip, final int port, final String password){
        boolean isRun = new IdempotentConfirmer(){
            private int timeOutFactor = 1;

            @Override
            public boolean execute(){
                WebJedis jedis = new WebJedis(ip, port);
                try{
                    if(!StringUtil.isEmpty(password)){
                        jedis.auth(password);
                    } //if
                    jedis.getClient().setConnectionTimeout(Protocol.DEFAULT_TIMEOUT * timeOutFactor);
                    jedis.getClient().setSoTimeout(Protocol.DEFAULT_TIMEOUT * timeOutFactor);
                    String pong = jedis.ping();
                    return pong != null && pong.equalsIgnoreCase("PONG");
                } catch (JedisDataException e){
                    String message = e.getMessage();
                    logger.warn(e.getMessage());
                    if(!StringUtil.isEmpty(message) && message.startsWith("LOADING")){
                        return true;
                    }
                    return false;
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    return false;
                }finally {
                    if(jedis != null){
                        try{
                            jedis.close();
                        } catch (Exception e){
                            logger.error(e.getMessage(), e);
                        }
                    }
                } //finally
            } //execute
        }.run();

        return isRun;
    }

    /**
     * 手动进行failover进行主从切换
     * */
    public boolean sentinelFailover(final String ip, final int port, final String masterName){
        boolean isSentinelRun = redisCenter.isInstanceRun(ip, port, "");
        if(!isSentinelRun){
            logger.error("ip:{}, port:{} is not run", ip, port);
            return false;
        }
        boolean isSentinelFailOver = new IdempotentConfirmer(){
            @Override
            public boolean execute() {
                WebJedis jedis = new WebJedis(ip, port, Protocol.DEFAULT_TIMEOUT);
                try{
                    String response = jedis.sentinelFailover(masterName);
                    return response != null && response.equalsIgnoreCase("OK");
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    return false;
                } finally {
                    if(jedis != null){
                        try{
                            jedis.close();
                        } catch (Exception e){
                            logger.error(e.getMessage(), e);
                        } //catch
                    } //if
                } //finally
            } //execute
        }.run();
        if(!isSentinelFailOver){
            logger.error("ip:{}, port:{} sentinel failover error", ip, port);
            return false;
        }else{
            logger.warn("ip:{}, port:{} sentinel failover success");
            return true;
        }
    }

    /**
     * 添加sentinel
     * */
    @Override
    public boolean addSentinel(String host, int port, String masterName, String password, String masterHost, int masterPort) {
        String sentinelConfFileName = RedisProtocol.getConfigFileName(port, ConstUtil.CACHE_REDIS_SENTINEL);
        List<String> sentinelConfigs = SentinelConfigUtil.getSentinelConfigs(masterHost, masterPort, password, masterName, host, port);
        String runShell = RedisProtocol.getRunShell(port, ConstUtil.CACHE_REDIS_SENTINEL);
        String machinePath = RedisProtocol.getMachinePath(port, ConstUtil.CACHE_REDIS_SENTINEL);
        boolean isPortUsed = AgentCommunication.isPortUsed(host, port);
        if(isPortUsed){
            logger.error("host:{}, port:{} is used", host, port);
            return false;
        }
        boolean isRunSuccess = AgentCommunication.runSentinel(host, port, sentinelConfFileName, sentinelConfigs, runShell, machinePath);
        if(!isRunSuccess){
            logger.error("add sentinel to host:{}, port:{} failed.", host, port);
            return false;
        }
        logger.info("add sentinel to host:{}, port:{} success.", host, port);

        return true;
    }

    /**
     * 添加slave
     * */
    @Override
    public boolean addSlave(String host, int port, String masterHost, int masterPort, String password) {
        boolean isPortUsed = AgentCommunication.isPortUsed(host, port);
        if(isPortUsed){
            logger.error("host:{}, port:{} is used.", host, port);
            return false;
        }
        logger.info("host:{}, port:{} is not used.", host, port);

        String confFileName = RedisProtocol.getConfigFileName(port, ConstUtil.CACHE_REDIS_STANDALONE);
        String runShell = RedisProtocol.getRunShell(port, ConstUtil.CACHE_REDIS_STANDALONE);
        String machinePath = RedisProtocol.getMachinePath(port, ConstUtil.CACHE_REDIS_STANDALONE);
        List<String> configs = RedisConfigUtil.getMinRedisInstanceConfig(port, password);
        String masterauth = "masterauth %s";
        masterauth = String.format(masterauth, password);
        configs.add(masterauth);

        boolean isRunSuccess = AgentCommunication.runInstance(host, port, ConstUtil.CACHE_REDIS_STANDALONE, password, confFileName, configs, runShell, machinePath);
        if(!isRunSuccess){
            logger.error("runInstance host:{}, port:{} failed.", host, port);
            return false;
        }
        logger.info("runInstance host:{}, port:{} success.", host, port);
        boolean isSlave = slaveOf(masterHost, masterPort, host, port, password);
        if(!isSlave){
            logger.error("slaveof masterhost:{}, masterport:{}, slavehost:{}, slaveport:{}", masterHost, masterPort, host, port);
            return false;
        }
        logger.info("add slave instance success, masterhost:{}, masterport:{}, slavehost:{}, slaveport:{}", masterHost, masterPort, host, port);
        return true;
    }

}