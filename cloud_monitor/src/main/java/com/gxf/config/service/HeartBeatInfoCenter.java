package com.gxf.config.service;

import com.gxf.udp.proto.HeartbeatInfo_Pb;

/**
 * Created by 58 on 2017/8/12.
 */
public interface HeartBeatInfoCenter {

    /**
     * 根据appkey生成心跳包
     * */
    HeartbeatInfo_Pb.HeartBeatInfo.Builder getHeartBeatInfoByAppKey(String appKey);
}
