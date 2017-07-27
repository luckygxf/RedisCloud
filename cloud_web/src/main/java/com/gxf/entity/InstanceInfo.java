package com.gxf.entity;

/**
 * Created by 58 on 2017/7/26.
 * redis实例
 */
public class InstanceInfo {
    //主键ID
    private int id;
    //实例ip
    private String host;
    //实例端口号
    private int port;
    //密码
    private String password;

    public InstanceInfo(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
