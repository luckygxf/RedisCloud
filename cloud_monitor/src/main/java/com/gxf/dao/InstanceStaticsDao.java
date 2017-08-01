package com.gxf.dao;

import com.gxf.entity.InstatnceStatics;

/**
 * Created by 58 on 2017/8/1.
 */
public interface InstanceStaticsDao {

    /**
     * 添加redis实例统计信息
     * */
    void add(InstatnceStatics instatnceStatics);
}
