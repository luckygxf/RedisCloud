package com.gxf.test;

import com.gxf.dao.MachineStaticsDao;
import com.gxf.dao.impl.MachineStaticsDaoImpl;
import com.gxf.entity.MachineStatics;

import java.util.Date;
import java.util.List;

/**
 * Created by 58 on 2017/7/31.
 */
public class Test {
    public static void main(String[] args) {
        MachineStaticsDao machineStaticsDao = new MachineStaticsDaoImpl();
//        MachineStatics machineStatics = new MachineStatics();
//        machineStatics.setIp("127.0.0.1");
//        machineStatics.setHostId(1);
//        machineStatics.setCpuUsage("ddd");
//        machineStatics.setLoad("ddd");
//        machineStatics.setTraffic("ddd");
//        machineStatics.setMemoryUsageRatio("ddd");
//        machineStatics.setMemoryFree("ddd");
//        machineStatics.setMemoryTotal("ddd");
//        machineStatics.setCreateTime(new Date());
//        machineStatics.setModifyTime(new Date());
//        machineStaticsDao.add(machineStatics);
        List<MachineStatics> list = machineStaticsDao.queryAll();
        for(MachineStatics machineStatics : list){
            System.out.println(machineStatics.getCreateTime());
        }
    }
}
