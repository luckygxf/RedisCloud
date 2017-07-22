package com.gxf.redis.util;

/**
 * Created by 58 on 2017/7/22.
 */
public class RedisClusterNode {
    //主节点地址
    private String masterHost;
    //主节点端口
    private int masterPort;
    //从节点地址
    private String slaveHost;
    //从节点端口
    private int slavePort;

    public RedisClusterNode(String masterHost, int masterPort, String slaveHost, int slavePort) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.slaveHost = slaveHost;
        this.slavePort = slavePort;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    public String getSlaveHost() {
        return slaveHost;
    }

    public void setSlaveHost(String slaveHost) {
        this.slaveHost = slaveHost;
    }

    public int getSlavePort() {
        return slavePort;
    }

    public void setSlavePort(int slavePort) {
        this.slavePort = slavePort;
    }
}
