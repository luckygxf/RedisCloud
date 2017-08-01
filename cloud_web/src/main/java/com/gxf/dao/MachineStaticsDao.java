package com.gxf.dao;

import com.gxf.entity.MachineStatics;

import java.util.List;

/**
 * Created by 58 on 2017/8/1.
 */
public interface MachineStaticsDao {

    /**
     * 查询所有的机器统计信息
     * */
    List<MachineStatics> queryAll();
}
