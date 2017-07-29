package com.gxf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/29.
 */
public class PropertiesHelper {

    public static Properties loadPropertiesFile(String fileName){
        Properties prop = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
