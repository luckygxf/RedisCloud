package com.gxf.redis;

import com.gxf.entity.InstatnceStatics;

/**
 * Created by 58 on 2017/8/2.
 */
public interface RedisTask {

    /**
     * 获取redis实例信息
     * */
    InstatnceStatics getRedisInstanceInfo(String ip, int port, String password);
}
