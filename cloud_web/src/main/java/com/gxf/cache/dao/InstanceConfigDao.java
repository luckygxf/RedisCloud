package com.gxf.cache.dao;

import com.gxf.entity.InstanceConfig;

import java.util.List;

/**
 * Created by 58 on 2017/7/15.
 * 配置模板dao
 */
public interface InstanceConfigDao {

    /**
     * 获取所有配置模板
     * */
    List<InstanceConfig> getAllInstanceConfig();
}
