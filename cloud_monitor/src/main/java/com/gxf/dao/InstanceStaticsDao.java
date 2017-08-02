package com.gxf.dao;

import com.gxf.entity.InstanceStatics;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 58 on 2017/8/1.
 */
public interface InstanceStaticsDao {

    /**
     * 添加redis实例统计信息
     * */
    void add(InstanceStatics instanceStatics);

    /**
     * 根据host和port查询
     * */
    InstanceStatics queryByHostAndPort(@Param("host") String host, @Param("port") int port);

    /**
     * 更新得到的数据
     * */
    void update(InstanceStatics instanceStatics);
}
