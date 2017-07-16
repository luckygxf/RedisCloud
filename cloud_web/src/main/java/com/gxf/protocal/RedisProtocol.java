package com.gxf.protocal;

import com.gxf.constant.EmptyObjectConst;
import com.gxf.util.TypeUtil;

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

    public String getMachinePath(int port, int type){
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
