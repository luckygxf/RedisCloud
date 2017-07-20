package com.gxf.redis.redisImpl;

import com.gxf.common.util.ConstUtil;
import com.gxf.communication.AgentCommunication;
import com.gxf.protocol.MachineProtocol;
import com.gxf.protocol.RedisProtocol;
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

    @Override
    public boolean deploySentinelInstance(long appId, String masterHost, String[] slaveHosts, int maxMemory, String[] sentinelIps, int appPort, List<WebJedis> jedisList) {
        String redisConfigFileName = RedisProtocol.getConfigFileName(appPort, ConstUtil.CACHE_REDIS_STANDALONE);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig();

        return true;
    }

    @Override
    public boolean deployRedisInstance(String host, int port, int type) {
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig();
        //redis-server %s &
        String shellTemplate = "redis-server %s &";
        String machinePath = RedisProtocol.getMachinePath(port, type);
        String configName = RedisProtocol.getConfigFileName(port, type);
        String runShell = String.format(shellTemplate, machinePath + configName);
        //password
        String password = PasswordUtil.genRandomNum(16);
        String passConfig = "requirepass " + password;
        redisConfigs.add(passConfig);
        //port
        String portConfig = "port " + port;
        redisConfigs.add(portConfig);

        boolean isSuccess = AgentCommunication.runInstance(host, port, type, password, configName, redisConfigs, runShell, machinePath);
        if(isSuccess){
            System.out.println("redis deploy success");
        }else{
            System.out.println("redis deploy fail");
        }
        return isSuccess;

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

    private String getMasterName(String host, int port){
        String masterSentinelName = String.format("sentinel-%S-%s", host, port);
        return masterSentinelName;
    }
}
