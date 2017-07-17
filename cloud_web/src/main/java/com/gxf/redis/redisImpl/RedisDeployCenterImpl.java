package com.gxf.redis.redisImpl;

import com.gxf.common.util.ConstUtil;
import com.gxf.communication.AgentCommunication;
import com.gxf.protocol.MachineProtocol;
import com.gxf.protocol.RedisProtocol;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.util.PasswordUtil;
import com.gxf.util.RedisConfigUtil;
import com.gxf.webJedis.WebJedis;

import java.util.List;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisDeployCenterImpl implements RedisDeployCenter {
    @Override
    public boolean deploySentinelInstance(long appId, String masterHost, String[] slaveHosts, int maxMemory, String[] sentinelIps, int appPort, List<WebJedis> jedisList) {
        String redisConfigFileName = RedisProtocol.getConfigFileName(appPort, ConstUtil.CACHE_REDIS_STANDALONE);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig();

        return true;
    }

    @Override
    public boolean deployRedisInstance(String host, int port, int type) {
        String redisConfFileName = RedisProtocol.getConfigFileName(port, type);
        List<String> redisConfigs = RedisConfigUtil.getMinRedisInstanceConfig();
        //redis-server %s &
        String shellTemplate = "redis-server %s &";
        String machinePath = RedisProtocol.getMachinePath(port, type);
        String configName = RedisProtocol.getConfigFileName(port, type);
        String runShell = String.format(shellTemplate, machinePath + configName);
        //password
        String password = PasswordUtil.genRandomNum(16);

        boolean isSuccess = AgentCommunication.runInstance(host, port, type, password, configName, redisConfigs, runShell, machinePath);
        if(isSuccess){
            System.out.println("redis deploy success");
        }else{
            System.out.println("redis deploy fail");
        }
        return isSuccess;

//        return true;
    }
}
