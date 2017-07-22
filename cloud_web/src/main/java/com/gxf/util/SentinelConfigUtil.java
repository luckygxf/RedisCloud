package com.gxf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/20.
 */
public class SentinelConfigUtil {
    private static String sentinelPortConfig = "port %d";
    private static String sentinelMonitorConfig = "sentinel monitor %s %s %d 1";


    public static void main(String[] args) {
        List<String> configs = getSentinelConfigs("127.0.0.1",111, "1111", "masterName", "127.0.0.1" , 26039);
        System.out.println(configs);
    }

    /**
     * 获取sentinel配置内容
     * deemonize yes
     * port %d
     * dir dir
     * sentinel monitor mastername masterhost masertport quorum
     * */
    public static List<String> getSentinelConfigs(String masterHost, int masterPort,  String password, String masterName,String sentinelIp, int sentinelPort){
        List<String> sentinelConfigs = new ArrayList<String>();
        sentinelConfigs.add("daemonize yes");
        sentinelConfigs.add("protected-mode no");
        sentinelConfigs.add(String.format(sentinelPortConfig, sentinelPort));
        sentinelConfigs.add(String.format(sentinelMonitorConfig, masterName, masterHost, masterPort));

        return sentinelConfigs;
    }
}
