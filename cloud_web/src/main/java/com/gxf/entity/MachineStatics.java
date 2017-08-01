package com.gxf.entity;

import java.util.Date;
import java.util.Map;

/**
 * Created by 58 on 2017/7/30.
 * 机器统计信息
 */
public class MachineStatics {
    private long id;

    private long hostId;

    private String ip;

    private String cpuUsage;

    private String load;

    private String traffic;

    private String memoryUsageRatio;

    private String memoryFree;

    private int memoryAllocated;

    private String memoryTotal;

    private Map<String/**挂载点*/, String/**使用百分比*/> diskUsageMap;

    private Date createTime;

    private Date modifyTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getMemoryUsageRatio() {
        return memoryUsageRatio;
    }

    public void setMemoryUsageRatio(String memoryUsageRatio) {
        this.memoryUsageRatio = memoryUsageRatio;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public int getMemoryAllocated() {
        return memoryAllocated;
    }

    public void setMemoryAllocated(int memoryAllocated) {
        this.memoryAllocated = memoryAllocated;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public Map<String, String> getDiskUsageMap() {
        return diskUsageMap;
    }

    public void setDiskUsageMap(Map<String, String> diskUsageMap) {
        this.diskUsageMap = diskUsageMap;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "MachineStatics{" +
                "id=" + id +
                ", hostId=" + hostId +
                ", ip='" + ip + '\'' +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", load='" + load + '\'' +
                ", traffic='" + traffic + '\'' +
                ", memoryUsageRatio='" + memoryUsageRatio + '\'' +
                ", memoryFree='" + memoryFree + '\'' +
                ", memoryAllocated=" + memoryAllocated +
                ", memoryTotal='" + memoryTotal + '\'' +
                ", diskUsageMap=" + diskUsageMap +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
