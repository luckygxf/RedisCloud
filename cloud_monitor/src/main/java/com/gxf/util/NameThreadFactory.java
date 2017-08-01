package com.gxf.util;

import java.util.concurrent.ThreadFactory;

/**
 * Created by 58 on 2017/8/1.
 */
public class NameThreadFactory implements ThreadFactory {
    private static String threadName = "redis_cloud_monitor";

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(null, r, threadName, 0);
        return thread;
    }
}
