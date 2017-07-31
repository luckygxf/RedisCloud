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
}
