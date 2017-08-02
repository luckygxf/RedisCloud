package com.gxf.util;

import mousio.etcd4j.EtcdClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/28.
 *
 */
public class EtcdUtil {
    private static final String App_Base_Path = "/com/gxf/redis/cloud/applist/";
    private static final String Machine_Base_Path = "/com/gxf/redis/cloud/machinelist/";
    private static Logger logger = LoggerFactory.getLogger(EtcdUtil.class);
    private static EtcdClient etcd;
    private static final String AddStatus = "Add";

    public static void main(String[] args) {
        createInstanceNode("127.0.0.1", 6543);
        deleteInstanceNode("127.0.0.1", 6543);
    }
    static {
        try{
            Properties properties = PropertiesHelper.loadPropertiesFile("etcd.properties");
            String etcdPath = (String) properties.get("etcd.path");
            String[] etcdPaths = etcdPath.split(";");
            URI[] etcdURIArray = new URI[etcdPaths.length];
            for(int i = 0; i < etcdPaths.length; i++){
                etcdURIArray[i] = URI.create(etcdPaths[i]);
            }
            etcd = new EtcdClient(etcdURIArray);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 创建redis实例对应的节点
     * ip + port
     * */
    public static void createInstanceNode(String host, int port){
        String hostAndPort = host + ":" + port;
        try {
            etcd.put(App_Base_Path + hostAndPort, AddStatus).send().get();
            logger.info("etcd create node success host:{}, port:{}", host, port);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("etcd create node fail host:{}, port:{}", host, port);
        }
    }

    /**
     * 删除redis实例对应的etcd节点
     * */
    public static void deleteInstanceNode(String host, int port){
        String hostAndPort = host + ":" + port;
        try {
            etcd.delete(App_Base_Path + host).send().get();
            logger.info("etcd delete node success, host:{}", host);
        } catch (Exception e) {
            logger.error("etcd delete node fail, host:{}", host);
            logger.error(e.getMessage(), e);
        }
    }


}
