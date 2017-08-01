package com.gxf.redis.impl;

import com.gxf.common.util.StringUtil;
import com.gxf.entity.InstatnceStatics;
import com.gxf.redis.RedisTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 58 on 2017/8/2.
 */
public class RedisTaskImpl implements RedisTask {
    private static Logger logger = LoggerFactory.getLogger(RedisTaskImpl.class);

    public static void main(String[] args) {
        String ip = "192.168.211.131";
        int port = 6340;
        String password = "vdrg3fbxcez0b72e";
        InstatnceStatics instatnceStatics = new RedisTaskImpl().getRedisInstanceInfo(ip, port, password);
    }

    /**
     * 收集redis实例信息
     * TODO:待完成
     * */
    public InstatnceStatics getRedisInstanceInfo(String ip, int port, String password) {
        String info = getRedisInfo(ip, port, password);
        //把所有info数据放到一个map里
        Map<String, String> redisStatMap = new HashMap<String, String>();
        String statData[] = info.split("\r\n");
        for(String statLine : statData){
            if(StringUtil.isEmpty(statLine) || statLine.indexOf("#") >= 0){
                continue;
            }
            if(statLine.contains(":")){
                String keyValue[] = statLine.split(":");
                redisStatMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return  processInfo(info);
    }

    /**
     * 处理info，放到map集合里面
     * */
    private Map<String, Map<String, String>> getRedisStatMap(String info){
        Map<String, Map<String, String>> redisStatMap = new HashMap<String, Map<String, String>>();
        //TODO:wredis这里做了分区处理，后面需要在研究一下
        return redisStatMap;
    }

    /**
     * 根据获取到的info信息，解析出需要的信息
     * TODO:待完成
     * */
    private InstatnceStatics processInfo(String info){
        InstatnceStatics instatnceStatics = new InstatnceStatics();

        return instatnceStatics;
    }

    /**
     * 对redis实例执行info命令
     * */
    private String getRedisInfo(String ip, int port, String password){
        String info = "";
        try{
            Jedis jedis = new Jedis(ip, port);
            if(!StringUtil.isEmpty(password)){
                jedis.auth(password);
            }
            info = jedis.info();
            jedis.close();
        } catch (Exception e){
            logger.error("get redis instance host:{}, port:{} info failed.", ip, port);
            logger.error(e.getMessage(), e);
        }
        return info;
    }
}
