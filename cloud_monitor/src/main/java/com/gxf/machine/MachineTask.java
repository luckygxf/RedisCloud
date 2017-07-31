package com.gxf.machine;

import com.gxf.entity.MachineStatics;

/**
 * Created by 58 on 2017/7/31.
 */
public interface MachineTask {

    /**
     * 收集机器信息
     * */
    MachineStatics getMachineInfo(String ip);
}
