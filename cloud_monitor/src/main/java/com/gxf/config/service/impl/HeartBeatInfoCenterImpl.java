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
        builder.setAppKey(appKey);
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
