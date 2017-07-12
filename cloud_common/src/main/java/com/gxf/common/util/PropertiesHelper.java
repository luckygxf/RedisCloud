package com.gxf.common.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/11.
 */
public class PropertiesHelper {
    public static Properties loadPropertiesFile(String fileName){
        Properties prop = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try{
            prop.load(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        }

        return prop;
    }
}
