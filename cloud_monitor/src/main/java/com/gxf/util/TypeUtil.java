package com.gxf.util;

import com.gxf.common.util.ConstUtil;

/**
 * Created by 58 on 2017/7/17.
 */
public class TypeUtil {

    public static boolean isRedisType(int type){
        if(type == ConstUtil.CACHE_REDIS_SENTINEL
                || type == ConstUtil.CACHE_TYPE_REDIS_CLUSTER
                || type == ConstUtil.CACHE_REDIS_STANDALONE){
            return true;
        }

        return false;
    }

    public static boolean isRedisCluster(int type){
        return type == ConstUtil.CACHE_TYPE_REDIS_CLUSTER;
    }

    public static boolean isRedisSentinel(int type){
        if(type == ConstUtil.CACHE_REDIS_SENTINEL){
            return true;
        }
        return false;
    }

    public static boolean isRedisStandalone(int type){
        return type == ConstUtil.CACHE_REDIS_STANDALONE;
    }

    public static boolean isRedisDataType(int type){
        if(type == ConstUtil.CACHE_TYPE_REDIS_CLUSTER
                || type == ConstUtil.CACHE_REDIS_STANDALONE){
            return true;
        }

        return false;
    }
}
