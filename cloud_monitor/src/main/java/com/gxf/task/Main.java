package com.gxf.task;

import com.gxf.machine.impl.MachineTaskImpl;
import com.gxf.redis.impl.RedisTaskImpl;
import com.gxf.util.EtcdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 58 on 2017/8/1.
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws UnknownHostException {
        //注册monitor到etcd
        InetAddress localAddress = InetAddress.getLocalHost();
        String host = localAddress.getHostAddress();
        EtcdUtil.createMonitor(host);

        //启动监控agent和redis实例
        MachineTaskImpl.start();
        RedisTaskImpl.start();
    }
}
