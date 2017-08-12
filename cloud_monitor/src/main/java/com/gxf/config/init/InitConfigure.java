package com.gxf.config.init;

import com.gxf.config.constants.ConstUtil;
import com.gxf.config.util.PropertiesHelper;

import java.util.Properties;

/**
 * Created by 58 on 2017/8/8.
 * 初始化
 */
public class InitConfigure {

    public static void init(){
        Properties properties = PropertiesHelper.loadProperties("config.properties");
        ConstUtil.configCenterListenPort = Integer.parseInt(properties.get("config.port").toString().trim());
        ConstUtil.monitorPort = Integer.parseInt((String) properties.get("monitor.port"));
    }
}
