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

    /**
     * 关闭对应的redis实例
     * master | slave | sentinel
     * */
    boolean shutdownRedis(String host, int port, String password);

    /**
     * 判断redis实例是否已经运行
     * master | slave | sentinel
     * */
    boolean isInstanceRun(String host, int port, String password);
}
