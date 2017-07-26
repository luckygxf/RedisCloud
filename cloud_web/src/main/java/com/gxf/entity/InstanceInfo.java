package com.gxf.entity;

/**
 * Created by 58 on 2017/7/26.
 */
public class InstanceInfo {
    //实例ip
    private String host;
    //实例端口号
    private int port;

    public InstanceInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public InstanceInfo() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
