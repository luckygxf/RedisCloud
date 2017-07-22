package com.gxf.redis.redisImpl;

import com.gxf.common.util.ConstUtil;
import com.gxf.common.util.IdempotentConfirmer;
import com.gxf.communication.AgentCommunication;
import com.gxf.protocol.Protocol;
import com.gxf.protocol.RedisProtocol;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.util.PasswordUtil;
import com.gxf.util.RedisConfigUtil;
import com.gxf.util.SentinelConfigUtil;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisDeployCenterImpl implements RedisDeployCenter {
    private static Logger logger = LoggerFactory.getLogger(RedisDeployCenterImpl.class);
    private static RedisCenter redisCenter = new RedisCenterImpl();

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
        String runShell = String.format(shellTemplate, machinePath + configName);
        String password = PasswordUtil.genRandomNum(16);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig(port, password);

        boolean isSuccess = AgentCommunication.runInstance(host, port, type, password, configName, redisConfigs, runShell, machinePath);
        if(isSuccess){
            System.out.println("redis deploy success");
        }else{
            System.out.println("redis deploy fail");
        }
        return isSuccess;

    }

    /**
     * TODO:待完成
     * */
    @Override
    public boolean deploySentinelModel(String masterHost, String[] slaveHosts,int type, String[] sentinelIps, int masterPort, int slavePort, int[] sentinelPorts, List<WebJedis> jedisList) {
        //1. 启动master实例
        String configFileName = RedisProtocol.getConfigFileName(masterPort, type);
        String password = PasswordUtil.genRandomNum(16);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig(masterPort, password);
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

        String masterauth = "masterauth %s";
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
            logger.error("slaveof masterHost:{}, masterPort:{}, slaveHost:{}, slavePort:{}", masterHost, masterPort
                            , slaveHost, slavePort);
            return false;
        }

        //TODO:运行sentinel组，即3个sentinel
        //1.4 运行sentinel group

        return true;
    }

    /**
     * 部署哨兵组
     * 这里由于在虚拟机中部署
     * ip是一个，端口有多个
     * */
    private boolean runSentinelGroup(String sentinelIp,int sentinelPorts[], String masterHost, int masterPort, String password, List<WebJedis> jedisList){
        String masterName = getMasterName(masterHost, masterPort);
        for(int i = 0; i < sentinelPorts.length; i++){
            sentinelPorts[i] += RedisProtocol.REDIS_SENTINEL_BASE_PORT + sentinelPorts[i];
        }
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
}
