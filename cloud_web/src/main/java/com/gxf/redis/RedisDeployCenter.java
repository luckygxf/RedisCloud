package com.gxf.redis;

import com.gxf.webJedis.WebJedis;

import java.util.List;

/**
 * Created by 58 on 2017/7/17.
 */
public interface RedisDeployCenter {

    /**
     * 部署redis sentinel实例 实例组
     * */
    boolean deploySentinelInstance(long appId, String masterHost, String[] slaveHosts, int maxMemory,
                                   String[] sentinelIps, int appPort, List<WebJedis> jedisList);

    /**
     * 根据端口号，启动redis实例
     * */
    boolean deployRedisInstance(String host, int port, int type);
}
