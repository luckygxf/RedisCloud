package com.gxf.util;

import java.util.List;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisConfigUtil {
    private static String redisMinFileName = "redis_min.conf";

    public static void main(String[] args) {
        List<String> configs = getMinRedisInstanceConfig();
        System.out.println(configs);
    }

    public static List<String> getMinRedisInstanceConfig(){
        String filePath = PathUtil.getFilePath(redisMinFileName);
        List<String> configs = FileUtil.getFileContent(filePath);

        return configs;
    }

}
