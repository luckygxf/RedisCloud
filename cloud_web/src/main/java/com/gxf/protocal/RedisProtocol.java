package com.gxf.protocal;

/**
 * Created by 58 on 2017/7/13.
 */
public class RedisProtocol {
    private static final String SENTINEL_REDIS_CONFIG = "redis%d.conf";

    /**
     * 获取sentinel模式下redis实例配置文件名
     * */
    public static String getConfigFileName(int port, int type){

        return String.format(SENTINEL_REDIS_CONFIG, port);
    }
}
