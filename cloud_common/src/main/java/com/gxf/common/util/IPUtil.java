package com.gxf.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 58 on 2017/8/1.
 */
public class IPUtil {
    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    /**
     * 获取机器本地IP地址
     * 避免获取到本地环回地址
     * */
    public static InetAddress getAddress() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    return addresses.nextElement();
                }
            }
        } catch (SocketException e) {
            logger.debug("Error when getting host ip address: <{}>.", e.getMessage());
        }
        return null;
    }
}
