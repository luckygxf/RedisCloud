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
    //运行状态
    private byte status;
    //appid
    private int appId;
    //master name
    private String masterName;
    //type
    private int type;


    public InstanceInfo(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public InstanceInfo(String host, int port, byte status, int appId, int type) {
        this.host = host;
        this.port = port;
        this.status = status;
        this.appId = appId;
        this.type = type;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
