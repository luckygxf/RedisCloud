package com.gxf.util;

import java.util.List;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisConfigUtil {
    private static String redisMinFileName = "redis_min.conf";

    public static void main(String[] args) {
        List<String> configs = getMinRedisInstanceConfig(6040, "ssdsfda");
        System.out.println(configs);
    }

    public static List<String> getMinRedisInstanceConfig(int port, String password){
        String filePath = PathUtil.getFilePath(redisMinFileName);
        List<String> configs = FileUtil.getFileContent(filePath);
        //password
        String passConfig = "requirepass " + password;
        configs.add(passConfig);
        //port
        String portConfig = "port " + port;
        configs.add(portConfig);
        return configs;
    }

}
