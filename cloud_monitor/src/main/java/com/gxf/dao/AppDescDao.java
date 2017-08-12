package com.gxf.dao;

import com.gxf.entity.AppDesc;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 58 on 2017/8/12.
 */
public interface AppDescDao {

    /**
     * 根据appkey查询appdesc
     * */
    AppDesc queryByAppkey(@Param("appKey")String appKey);
}
