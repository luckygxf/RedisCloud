package com.gxf.util;

import mousio.etcd4j.EtcdClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.Properties;


/**
 * Created by 58 on 2017/7/30.
 */
public class EtcdUtil {
    private static Logger logger = LoggerFactory.getLogger(EtcdUtil.class);
    private static final String Etcd_Machine_Bath_Path = "/com/gxf/redis/cloud/machinelist/";
    private static final String Status_Add = "add";
    private static EtcdClient etcd;

    public static void main(String[] args) {
        createMachineNode("192.168.211.131");
    }

    static {
        try{
            Properties properties = PropertiesHelper.loadProperties("etcd.properties");
            String etcdPath = (String) properties.get("etcd.path");
            String[] etcdPathArray = etcdPath.split(";");

            URI[] etcdURIArray = new URI[etcdPathArray.length];
            for(int i = 0; i < etcdPathArray.length; i++){
                etcdURIArray[i] = URI.create(etcdPathArray[i]);
            }
            etcd = new EtcdClient(etcdURIArray);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * agent注册到etcd machinelist
     * */
    public static void createMachineNode(String host){
        try {
            etcd.put(Etcd_Machine_Bath_Path + host, Status_Add).send().get();
            logger.info("add machine list success, host:{}", host);
        }  catch (Exception e) {
            logger.error("add machine list failed, host:{}", host);
        }
    }
}
