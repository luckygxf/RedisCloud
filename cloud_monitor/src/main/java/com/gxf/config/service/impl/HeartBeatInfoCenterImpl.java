package com.gxf.config.service.impl;

import com.gxf.common.util.StringUtil;
import com.gxf.config.constants.ConstUtil;
import com.gxf.config.service.HeartBeatInfoCenter;
import com.gxf.dao.AppDescDao;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.dao.impl.AppDescDaoImpl;
import com.gxf.dao.impl.InstanceInfoDaoImpl;
import com.gxf.entity.AppDesc;
import com.gxf.entity.InstanceInfo;
import com.gxf.udp.proto.HeartbeatInfo_Pb;
import com.gxf.util.EtcdUtil;
import com.gxf.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 58 on 2017/8/12.
 */
public class HeartBeatInfoCenterImpl implements HeartBeatInfoCenter {
    private Logger logger = LoggerFactory.getLogger(HeartBeatInfoCenterImpl.class);
    private AppDescDao appDescDao = new AppDescDaoImpl();
    private InstanceInfoDao instanceInfoDao = new InstanceInfoDaoImpl();

    //需要返回客户端的sentinels masterName type password
    //sentinels格式ip:port, ip:port..
    public HeartbeatInfo_Pb.HeartBeatInfo.Builder getHeartBeatInfoByAppKey(String appKey) {
        AppDesc appDesc = appDescDao.queryByAppkey(appKey);
        if(appDesc.getType() == com.gxf.common.util.ConstUtil.CACHE_REDIS_SENTINEL){
            return getSentinelHeartBeatInfo(appDesc);
        }else {
            return getClusterHeartBeatInfo(appDesc);
        }
    }

    /**
     * 对集群模式的app信息
     * */
    private HeartbeatInfo_Pb.HeartBeatInfo.Builder getClusterHeartBeatInfo(AppDesc appDesc){
        //集群客户端需要type
        //shardinfo ip1:port1,ip2:port2..
        //reportIp ip:port
        List<InstanceInfo> instanceInfoList = instanceInfoDao.queryByAppId(appDesc.getAppId());
        //获取shardInfo
        StringBuilder shardInfo = new StringBuilder();
        for (int i = 0; i < instanceInfoList.size(); i++){
            InstanceInfo instanceInfo = instanceInfoList.get(i);
            if(!TypeUtil.isRedisCluster(instanceInfo.getType())){
                continue;
            } //if
            shardInfo.append(instanceInfo.getHost());
            shardInfo.append(":");
            shardInfo.append(instanceInfo.getPort());
            if(i != instanceInfoList.size() - 1){
                shardInfo.append(",");
            }
        } //for
        HeartbeatInfo_Pb.HeartBeatInfo.Builder heartBeatInfoBuilder = HeartbeatInfo_Pb.HeartBeatInfo.newBuilder();
        //设置shardInfo
        heartBeatInfoBuilder.setShardInfo(shardInfo.toString());
        //设置type
        heartBeatInfoBuilder.setType(appDesc.getType());
        //设置reportIp
        StringBuilder reportIp = new StringBuilder(EtcdUtil.getMonitor());
        reportIp.append(":");
        reportIp.append(ConstUtil.monitorPort);
        heartBeatInfoBuilder.setReportIp(reportIp.toString());
        //设置appkey
        heartBeatInfoBuilder.setAppKey(appDesc.getAppKey());
        return heartBeatInfoBuilder;
    }

    /**
     * 获取哨兵模式的app信息
     * */
    private HeartbeatInfo_Pb.HeartBeatInfo.Builder getSentinelHeartBeatInfo(AppDesc appDesc){
        List<InstanceInfo> listOfInstanceInfo = instanceInfoDao.queryByAppId(appDesc.getAppId());
        StringBuilder sentinels = new StringBuilder();
        String masterName = "";
        for(int i = 0; i < listOfInstanceInfo.size(); i++){
            InstanceInfo instanceInfo = listOfInstanceInfo.get(i);
            if(!TypeUtil.isRedisSentinel(instanceInfo.getType())){
                continue;
            }
            if(StringUtil.isEmpty(masterName)){
                masterName = instanceInfo.getMasterName();
            }
            sentinels.append(listOfInstanceInfo.get(i).getHost());
            sentinels.append(":");
            sentinels.append(listOfInstanceInfo.get(i).getPort());
            if(i != listOfInstanceInfo.size() - 1){
                sentinels.append(",");
            } //if
        } //for

        HeartbeatInfo_Pb.HeartBeatInfo.Builder builder = HeartbeatInfo_Pb.HeartBeatInfo.newBuilder();
        builder.setAppKey(appDesc.getAppKey());
        builder.setSentinels(sentinels.toString());
        builder.setMasterName(masterName);
        builder.setType(appDesc.getType());
        builder.setPassword(appDesc.getPassword());
        //reportIp格式ip:port
        StringBuilder reportIp = new StringBuilder(EtcdUtil.getMonitor());
        reportIp.append(":");
        reportIp.append(ConstUtil.monitorPort);
        builder.setReportIp(reportIp.toString());
        return builder;
    }
}
