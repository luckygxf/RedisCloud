package com.gxf.common.util;

/**
 * Created by 58 on 2017/7/11.
 */
public class ConstUtil {
    public static final int AGENT_UDP_PORT = Integer.parseInt(PropertiesHelper.loadPropertiesFile("agent.properties").getProperty("agent_udp_port"));
}
