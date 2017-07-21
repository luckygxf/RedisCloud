package com.gxf.redis;

/**
 * Created by 58 on 2017/7/21.
 */
public interface RedisCenter {

    /**
     * 重写配置
     * config rewrite
     * */
    boolean configRewreite(String host, int port, String password);
}
