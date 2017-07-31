package com.gxf.test;

import com.gxf.dao.MachineStaticsDao;
import com.gxf.dao.impl.MachineStaticsDaoImpl;
import com.gxf.entity.MachineStatics;
import com.gxf.machine.MachineTask;
import com.gxf.machine.impl.MachineTaskImpl;

import java.util.Date;
import java.util.List;

/**
 * Created by 58 on 2017/7/31.
 */
public class Test {
    public static void main(String[] args) {
        String ip = "192.168.211.131";
        MachineStaticsDao machineStaticsDao = new MachineStaticsDaoImpl();
        MachineTask machineTask = new MachineTaskImpl();
        MachineStatics machineStatics = machineTask.getMachineInfo(ip);
        machineStaticsDao.add(machineStatics);
    }
}
