package com.gxf.init;

import com.gxf.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by 58 on 2017/7/31.
 * 进行配置初始化 mybatis
 */
public class InitionConfigure {
    private static Logger logger = LoggerFactory.getLogger(InitionConfigure.class);

    /**
     * 进行初始化
     * */
    public static void init(){
        initMybatis();
    }

    /**
     * 初始化mybatis
     * */
    private static void initMybatis(){
        logger.info("start init mybatis config");
        String configFileName = "mybatis-config.xml";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
        SqlSessionFactoryUtil.sessionFactiory = new SqlSessionFactoryBuilder().build(inputStream);
    }
}
