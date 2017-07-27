package com.gxf.dao;

import com.gxf.entity.InstanceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 58 on 2017/7/27.
 */
@Mapper
public interface InstanceInfoDao {

    /**
     * 添加redis instance信息
     * */
    void addInstanceInfo(InstanceInfo instanceInfo);
}
