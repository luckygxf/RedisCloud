package com.gxf.protocol;

import com.gxf.common.util.ConstUtil;
import com.gxf.constant.EmptyObjectConst;
import com.gxf.util.DateUtil;
import com.gxf.util.TypeUtil;

import java.util.Date;

/**
 * Created by 58 on 2017/7/13.
 */
public class RedisProtocol {
    private static final String SENTINEL_REDIS_CONFIG = "redis%d.conf";
    private static final String SENTINEL_CONFIG = "sentinel%d.conf";
    private static final String SENTINEL_SHELL = ConstUtil.SENTINEL_CMD_PREFIX+"redis-server %s --sentinel > " + "%s" + "redis-sentinel-%d-%s.log 2>&1 &";
    private static final String CLUSTER_CONFIG = "rcn%d.conf";
    private static final String RUN_SHELL = "%sredis-server %s > " + "%s" + "redis-%d-%s.log 2>&1 &";
    public static final int REDIS_SENTINEL_BASE_PORT = 20000;

    /**
     * 获取sentinel模式下redis实例配置文件名
     * */
    public static String getConfigFileName(int port, int type){
        if(TypeUtil.isRedisCluster(type)){
            return String.format(CLUSTER_CONFIG, port);
        }else if(TypeUtil.isRedisSentinel(type)){
            return String.format(SENTINEL_CONFIG, port);
        }else if(TypeUtil.isRedisStandalone(type)){
            return String.format(SENTINEL_REDIS_CONFIG, port);
        }

        return EmptyObjectConst.EMPTY_STRING;
    }

    /**
     * 根据端口和类型获取对应的shell脚本
     * */
    public static String getRunShell(int port, int type){
        if(TypeUtil.isRedisCluster(type)){
            return String.format(RUN_SHELL, ConstUtil.CLUSTER_CMD_PREFIX, RedisProtocol.getMachinePath(port, type) + getConfigFileName(port, type),
                            getMachinePath(port, type), port, DateUtil.formatYYYYMMddHHMM(new Date()));
        }else if(TypeUtil.isRedisSentinel(type)){
            return String.format(SENTINEL_SHELL, RedisProtocol.getMachinePath(port, ConstUtil.CACHE_REDIS_SENTINEL) + getConfigFileName(port, type),
                            getMachinePath(port,ConstUtil.CACHE_REDIS_SENTINEL), port, DateUtil.formatYYYYMMddHHMM(new Date()));
        }else{
            return String.format(RUN_SHELL, ConstUtil.SENTINEL_CMD_PREFIX, RedisProtocol.getMachinePath(port, type) + getConfigFileName(port, type),
                            getMachinePath(port, type), port, DateUtil.formatYYYYMMddHHMM(new Date()));
        }
    }

    /**
     * 机器上的部署路径
     * */
    public static String getMachinePath(int port, int type){
        if(TypeUtil.isRedisCluster(type)){
            return String.format(MachineProtocol.CLUSTER_DIR, port, port);
        }else if(TypeUtil.isRedisSentinel(type)){
            return String.format(MachineProtocol.SENTINEL_DIR, port);
        }else if(TypeUtil.isRedisStandalone(type)){
            return String.format(MachineProtocol.SENTINEL_REDIS_DIR, port);
        }

        return EmptyObjectConst.EMPTY_STRING;
    }
}
