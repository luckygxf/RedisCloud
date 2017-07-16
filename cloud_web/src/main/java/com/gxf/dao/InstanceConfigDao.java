package com.gxf.dao;

import com.gxf.entity.InstanceConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by 58 on 2017/7/16.
 */
@Mapper
public interface InstanceConfigDao {

    /**
     * 查询所有记录
     * */
//    @Select("select * from instance_config")
    List<InstanceConfig> queryAll();
}
