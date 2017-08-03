package com.gxf.util;

import mousio.client.promises.ResponsePromise;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by 58 on 2017/7/30.
 */
public class EtcdUtil {
    private static Logger logger = LoggerFactory.getLogger(EtcdUtil.class);
    private static final String Etcd_Machine_Bath_Path = "/com/gxf/redis/cloud/machinelist/";
    private static final String Etcd_App_Bath_Path = "/com/gxf/redis/cloud/applist/";
    private static final String Status_Add = "add";
    private static EtcdClient etcd;

    public static void main(String[] args) {
//        createMachineNode("192.168.211.131");
//        List<HostAndPort> list = getAllAppList();
//        for(HostAndPort hostAndPort : list){
//            System.out.println(hostAndPort.getHost() + ":" + hostAndPort.getPort());
//        }
        waitForMachineListChange();
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

    /**
     * 获取所有需要监控的IP
     * 这里可以多装几个虚拟机
     * */
    public static List<String> getAllMachineIP(){
        List<String> machines = new ArrayList<String>();
        try {
            EtcdResponsePromise<EtcdKeysResponse> etcdResponsePromise = etcd.getDir(Etcd_Machine_Bath_Path).recursive().send();
            EtcdKeysResponse response = etcdResponsePromise.get();
            EtcdKeysResponse.EtcdNode node = response.getNode();
            List<EtcdKeysResponse.EtcdNode> listNodes = node.getNodes();
            for (EtcdKeysResponse.EtcdNode node1 : listNodes){
                machines.add(node1.getKey().substring(Etcd_Machine_Bath_Path.length()));
                System.out.println(node1.getKey() + ", " + node1.getValue());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return machines;
    }

    /**
     * 获取注册在etcd里面的app节点
     * */
    public static List<HostAndPort> getAllAppList(){
        List<HostAndPort> appList = new ArrayList<HostAndPort>();
        try {
            EtcdResponsePromise<EtcdKeysResponse> etcdResponsePromise = etcd.getDir(Etcd_App_Bath_Path).recursive().send();
            EtcdKeysResponse response = etcdResponsePromise.get();
            EtcdKeysResponse.EtcdNode node = response.getNode();
            List<EtcdKeysResponse.EtcdNode> listNodes = node.getNodes();
            for (EtcdKeysResponse.EtcdNode node1 : listNodes){
                String hostAndPort = node1.getKey().substring(Etcd_App_Bath_Path.length());
                String host = hostAndPort.split(":")[0];
                int port = Integer.valueOf(hostAndPort.split(":")[1].trim());
                appList.add(new HostAndPort(host, port));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return appList;
    }

    /**
     * 监听machine list改变事件
     * */
    public static void waitForMachineListChange(){
        EtcdResponsePromise promise = null;
        try{
            System.out.println("start listen..");
            promise = etcd.get(Etcd_Machine_Bath_Path).recursive().waitForChange().send();
            promise.addListener(new ModifyHandler(promise));
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * etcd key改变回调处理
     * */
    static class ModifyHandler implements ResponsePromise.IsSimplePromiseResponseHandler{
        private EtcdResponsePromise promise;

        public ModifyHandler(EtcdResponsePromise promise) {
            this.promise = promise;
        }

        public void onResponse(ResponsePromise response) {
            System.out.println("receive from etcd server");
            System.out.println(response);
            EtcdKeysResponse etcdKeysResponse = (EtcdKeysResponse) response.getNow();
            System.out.println(etcdKeysResponse.getNode().getValue());
            try {
                promise = etcd.get(Etcd_Machine_Bath_Path).recursive().waitForChange().send();
                promise.addListener(new ModifyHandler(promise));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}


