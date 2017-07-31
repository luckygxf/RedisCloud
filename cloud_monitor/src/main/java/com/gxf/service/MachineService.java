package com.gxf.service;

/**
 * Created by 58 on 2017/8/1.
 */
public interface MachineService {

    /**
     * 收集机器信息并保存到数据库
     * */
    void collectMachineInfo(String ip);
}
