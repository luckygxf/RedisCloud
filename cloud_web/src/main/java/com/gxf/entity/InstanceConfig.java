package com.gxf.entity;

import java.util.Date;

/**
 * Created by 58 on 2017/7/15.
 * redis实例配置模板
 */
public class InstanceConfig {

    private long id;

    private String configKey;

    private String configValue;

    private String info;

    private Date updateTime;

    /**
     * redis类型(参考ConstUtil)
     * */
    private int type;

    /**
     * 1 有效
     * 0 无效
     * */
    private int status;

    @Override
    public String toString() {
        return "InstanceConfig{" +
                "id=" + id +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", info='" + info + '\'' +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", status=" + status +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
