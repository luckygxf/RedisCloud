package com.gxf.dao;

import com.gxf.entity.AppDesc;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 58 on 2017/8/3.
 */
public interface AppDescDao {

    /**
     * 添加应用描述
     * */
    void add(AppDesc appDesc);

    /**
     * 根据appid查询app_desc
     * */
    AppDesc queryByAppid(@Param("appid") int appid);

    /**
     * 获取最大的appid
     * */
    int getMaxAppId();
}
