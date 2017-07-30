package com.gxf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/30.
 */
public class PropertiesHelper {

    private static Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    /**
     * 加载配置文件中的配置
     * */
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return properties;
    }
}
