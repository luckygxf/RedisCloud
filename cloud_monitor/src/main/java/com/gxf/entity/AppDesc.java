package com.gxf.entity;

import java.util.Date;

/**
 * Created by 58 on 2017/8/3.
 */
public class AppDesc {
    private int appId;
    private String name;
    private int appPort;
    private int status;
    private String intro;
    private Date createTime;
    private String memSize;
    private int type;
    private String password;
    private String appKey;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppPort() {
        return appPort;
    }

    public void setAppPort(int appPort) {
        this.appPort = appPort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMemSize() {
        return memSize;
    }

    public void setMemSize(String memSize) {
        this.memSize = memSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "AppDesc{" +
                "appId=" + appId +
                ", name='" + name + '\'' +
                ", appPort=" + appPort +
                ", status=" + status +
                ", intro='" + intro + '\'' +
                ", createTime=" + createTime +
                ", memSize='" + memSize + '\'' +
                ", type=" + type +
                ", password='" + password + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
