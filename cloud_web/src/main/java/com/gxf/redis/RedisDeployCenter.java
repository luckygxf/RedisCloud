package com.gxf.redis;

import com.gxf.redis.util.RedisClusterNode;
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

    /**
     * 针对虚拟机部署，特殊需求
     * */
    boolean deploySentinelModel(int appid, String masterHost, String[] slaveHosts, int type, String[] sentinelIps, int masterPort, int slavePort, int sentinelPorts[], List<WebJedis> jedisList);

    /**
     * 针对虚拟机部署集群
     * */
    boolean deployCluster(String masterHost, String slaveHost, int masterPorts[], int slavePorts[], int appId);

    /**
     * 部署redis集群
     * */
    boolean deployClusterInstance(List<RedisClusterNode> nodes, int appId);

    /**
     * 下线指定的实例
     * */
    boolean shutdown(String ip, int port, String password);

    /**
     * 判断指定的实例是否运行
     * */
    boolean isRun(String ip, int port, String password);

    /**
     * 手动进行failover进行主从切换
     * */
    boolean sentinelFailover(String ip, int port, String masterName);

    /**
     * 添加sentinel
     * */
    boolean addSentinel(String host, int port, String masterName, String password, String masterHost, int masterPort);

    /**
     * 添加slave
     * */
    boolean addSlave(String host, int port, String masterHost, int masterPort, String password);
}
