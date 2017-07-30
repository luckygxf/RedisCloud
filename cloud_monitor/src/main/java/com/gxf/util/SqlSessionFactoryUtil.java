package com.gxf.util;

import com.gxf.init.InitionConfigure;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Created by 58 on 2017/7/31.
 */
public class SqlSessionFactoryUtil {
    public static SqlSessionFactory sessionFactiory;

    static {
        InitionConfigure.init();
    }
}
