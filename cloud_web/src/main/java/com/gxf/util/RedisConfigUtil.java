package com.gxf.util;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/17.
 */
public class RedisConfigUtil {
    private static String redisMinFileName = "redis_min.conf";

    public static void main(String[] args) {
        List<String> configs = getMinRedisInstanceConfig(6040, "ssdsfda");
        System.out.println(configs);
    }

    public static List<String> getMinRedisInstanceConfig(int port, String password){
        String filePath = PathUtil.getFilePath(redisMinFileName);
        List<String> configs = FileUtil.getFileContent(filePath);
        //password
        String passConfig = "requirepass " + password;
        configs.add(passConfig);
        //port
        String portConfig = "port " + port;
        configs.add(portConfig);
        return configs;
    }

    /**
     *
         bind 0.0.0.0
         port 7000
         cluster-enabled yes
         cluster-config-file nodes.conf
         cluster-node-timeout 5000
         appendonly yes
     * */
    public static List<String> getMinRedisClusterInstanceConfig(int port, String confFileName){
        List<String> clusterIntanceConfigs = new ArrayList<String>();

        String bindConfig = "bind 0.0.0.0";
        clusterIntanceConfigs.add(bindConfig);

        String listenPort = "port %d";
        listenPort = String.format(listenPort, port);
        clusterIntanceConfigs.add(listenPort);

        String enableCluster = "cluster-enabled yes";
        clusterIntanceConfigs.add(enableCluster);

        String confFileNameConf = "cluster-config-file node-%d.conf";
        confFileNameConf = String.format(confFileNameConf, port);
        clusterIntanceConfigs.add(confFileNameConf);

        String timeOut = "cluster-node-timeout 5000";
        clusterIntanceConfigs.add(timeOut);

        String appendOnly = "appendonly yes";
        clusterIntanceConfigs.add(appendOnly);

        return clusterIntanceConfigs;
    }

}
