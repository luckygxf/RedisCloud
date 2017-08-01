package com.gxf.entity;

import java.util.Date;

/**
 * Created by 58 on 2017/8/1.
 * Redis实例统计数据
 */
public class InstatnceStatics {
    private long id;
    private long instId;
    private long appId;
    private long hostId;
    private String ip;
    private int port;
    private byte role;
    private long maxMemory;
    private long usedMemory;
    private long currItems;
    private int currConnections;
    private long misses;
    private long hits;
    private Date modifyTime;
    private Date createTime;
    private byte isRun;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInstId() {
        return instId;
    }

    public void setInstId(long instId) {
        this.instId = instId;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte getRole() {
        return role;
    }

    public void setRole(byte role) {
        this.role = role;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getCurrItems() {
        return currItems;
    }

    public void setCurrItems(long currItems) {
        this.currItems = currItems;
    }

    public int getCurrConnections() {
        return currConnections;
    }

    public void setCurrConnections(int currConnections) {
        this.currConnections = currConnections;
    }

    public long getMisses() {
        return misses;
    }

    public void setMisses(long misses) {
        this.misses = misses;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public byte getIsRun() {
        return isRun;
    }

    public void setIsRun(byte isRun) {
        this.isRun = isRun;
    }

    @Override
    public String toString() {
        return "InstatnceStatics{" +
                "id=" + id +
                ", instId=" + instId +
                ", appId=" + appId +
                ", hostId=" + hostId +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", role=" + role +
                ", maxMemory=" + maxMemory +
                ", usedMemory=" + usedMemory +
                ", currItems=" + currItems +
                ", currConnections=" + currConnections +
                ", misses=" + misses +
                ", hits=" + hits +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                ", isRun=" + isRun +
                '}';
    }
}
