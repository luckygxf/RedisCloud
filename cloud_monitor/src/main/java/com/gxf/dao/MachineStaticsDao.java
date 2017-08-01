package com.gxf.dao;

import com.gxf.entity.MachineStatics;

import java.util.List;

/**
 * Created by 58 on 2017/7/31.
 */
public interface MachineStaticsDao {

    /**
     * 添加machinestatics到数据库
     * */
    void add(MachineStatics machineStatics);

    /**
     * 获取所有机器统计信息
     * */
    List<MachineStatics> queryAll();

    /**
     * 更新机器统计信息
     * */
    void updateMachineStatics(MachineStatics machineStatics);

    /**
     * 根据IP查询机器收集信息
     * */
    MachineStatics queryMachineStaticsByIp(String ip);
}
