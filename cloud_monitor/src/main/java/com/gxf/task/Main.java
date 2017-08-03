package com.gxf.task;

import com.gxf.machine.impl.MachineTaskImpl;
import com.gxf.redis.impl.RedisTaskImpl;

/**
 * Created by 58 on 2017/8/1.
 */
public class Main {

    public static void main(String[] args) {
        MachineTaskImpl.start();
        RedisTaskImpl.start();
    }
}
