package com.gxf.service.impl;

import com.gxf.dao.MachineStaticsDao;
import com.gxf.dao.impl.MachineStaticsDaoImpl;
import com.gxf.entity.MachineStatics;
import com.gxf.machine.MachineTask;
import com.gxf.machine.impl.MachineTaskImpl;
import com.gxf.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 58 on 2017/8/1.
 */
public class MachineServiceImpl implements MachineService {
    private MachineTask machineTask = new MachineTaskImpl();
    private MachineStaticsDao machineStaticsDao = new MachineStaticsDaoImpl();
    private static Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

    /**
     * 收集机器信息
     * */
    public void collectMachineInfo(String ip) {
        MachineStatics machineStatics = machineTask.getMachineInfo(ip);
        machineStaticsDao.add(machineStatics);
    }
}
