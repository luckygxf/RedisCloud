package com.gxf.test;

import redis.clients.jedis.Jedis;

/**
 * Created by 58 on 2017/7/31.
 */
public class Test {
    public static void main(String[] args) {
        String ip = "192.168.211.131";
        int port = 6340;
//        MachineStaticsDao machineStaticsDao = new MachineStaticsDaoImpl();
//        MachineTask machineTask = new MachineTaskImpl();
//        MachineStatics machineStatics = machineStaticsDao.queryMachineStaticsByIp(ip);
//        System.out.println(new Date());
//        machineStatics.setModifyTime(new Date());
//        machineStatics.setLoad("444");
//        System.out.println(machineStatics.getCpuUsage());
//        machineStaticsDao.updateMachineStatics(machineStatics);
//        machineStaticsDao.add(machineStatics);
//        InstanceStaticsDao instanceStaticsDao = new InstanceStaticsDaoImpl();
//        InstanceStatics instatnceStatics = new InstanceStatics();
//        instatnceStatics.setIp(ip);
//        instanceStaticsDao.add(instatnceStatics);
        Jedis jedis = new Jedis(ip, port);
        jedis.auth("1w24ywzeap2dve0a");
        System.out.println(jedis.info());
    }
}
