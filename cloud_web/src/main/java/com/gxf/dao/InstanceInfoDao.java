package com.gxf.dao;

import com.gxf.entity.InstanceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 58 on 2017/7/27.
 */
@Mapper
public interface InstanceInfoDao {

    /**
     * 添加redis instance信息
     * */
    void addInstanceInfo(InstanceInfo instanceInfo);


    /**
     * 根据host和port查询
     * */
    InstanceInfo queryByHostAndPort(@Param("host")String host, @Param("port")int port);

    /**
     * 更新记录
     * */
    void update(InstanceInfo instanceInfo);

    /**
     * 查询所有记录
     * */
    List<InstanceInfo> queryAll();

    /**
     * 根据id查询记录
     * */
    InstanceInfo queryById(@Param("id")int id);
}
