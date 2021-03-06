package com.gxf.dao;

import com.gxf.entity.InstanceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 58 on 2017/7/27.
 */
public interface InstanceInfoDao {

    /**
     * 添加redis instance信息
     * */
    void addInstanceInfo(InstanceInfo instanceInfo);

    /**
     * 根据host和port查询InstanceInfo
     * */
    InstanceInfo queryInstanceInfoByHostAndPort(@Param("host") String host, @Param("port") int port);

    /**
     * 根据appid查询实例信息
     * */
    List<InstanceInfo> queryByAppId(@Param("appId") int appId);
}
