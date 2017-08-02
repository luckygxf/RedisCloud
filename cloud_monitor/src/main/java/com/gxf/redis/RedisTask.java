package com.gxf.redis;

import com.gxf.entity.InstanceStatics;

/**
 * Created by 58 on 2017/8/2.
 */
public interface RedisTask extends Runnable {

    /**
     * 获取redis实例信息
     * */
    InstanceStatics getRedisInstanceInfo(String ip, int port, String password);
}
