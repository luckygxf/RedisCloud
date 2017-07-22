package com.gxf.common.util;

/**
 * Created by 58 on 2017/7/11.
 */
public class ConstUtil {
    //agent端口号
    public static final int AGENT_UDP_PORT = Integer.parseInt(PropertiesHelper.loadPropertiesFile("agent.properties").getProperty("agent_udp_port"));

    //redis类型
    public static final int CACHE_TYPE_REDIS_CLUSTER = 2;
    public static final int CACHE_REDIS_SENTINEL = 5;
    public static final int CACHE_REDIS_STANDALONE = 6;
    public static final int CACHE_REDIS_CLOUD = 8;

    public static final String SPACE = " ";

    public static String SENTINEL_CMD_PREFIX = "/opt/soft/redis403/";
    public static String CLUSTER_CMD_PREFIX = "/opt/soft/redis403/";


}
