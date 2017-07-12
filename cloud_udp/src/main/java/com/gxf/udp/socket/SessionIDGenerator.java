package com.gxf.udp.socket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 58 on 2017/7/11.
 */
public class SessionIDGenerator {
    private static final int MAX_SESSIONID = 1024 * 1024 * 1024;
    private static AtomicInteger managerSessoinId = new AtomicInteger(1);

    public static int createSessionId(){
        int value = managerSessoinId.getAndIncrement();
        if(value < MAX_SESSIONID){
            return value;
        }else if(value >= MAX_SESSIONID){
            managerSessoinId.set(1);
            value = managerSessoinId.getAndIncrement();
        }
        return value;
    }

}
