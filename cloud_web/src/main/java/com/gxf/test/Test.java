package com.gxf.test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.gxf.common.util.ArrayUtil;
import com.gxf.common.util.ConstUtil;
import com.gxf.machine.MachineCenter;
import com.gxf.machine.impl.MachineCenterImpl;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.redis.redisImpl.RedisCenterImpl;
import com.gxf.redis.redisImpl.RedisDeployCenterImpl;
import com.gxf.udp.proto.UDPServerObject_Pb;
import com.gxf.util.FileUtil;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/11.
 */
public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    private static String ip = "192.168.211.131";
//    private static String ip = "127.0.0.1";
    private static int port = 6379;
    //TODO:这里可以考虑用spring注入
    private static RedisDeployCenter redisDeployCenter = new RedisDeployCenterImpl();
    private static MachineCenter machineCenter = new MachineCenterImpl();

    public static void main(String[] args) throws InvalidProtocolBufferException {
//        shutdownInstance("192.168.211.131", 6042, "pd25aggrzjhtctch");
//        deployRedisInstance(ip, port, ConstUtil.CACHE_REDIS_STANDALONE);
//        testDeployCluster();
//        String password = "pd25aggrzjhtctch";
//        startRedisInstanceAtPort(ip, port, ConstUtil.CACHE_REDIS_STANDALONE, password);
//        testSentinelFailover();
//        testAddSentinel();
        testDeploySentinel();
//        testAddSlave();
//        testConnectRedis();
    }

    private static void testDeployCluster(){
        String masterHost = "192.168.211.131";
        String slaveHost = "192.168.211.131";
        int masterPorts[] = {6440, 6441, 6442};
        int slavePorts[] = {6543, 6544, 6545};

        boolean result = redisDeployCenter.deployCluster(masterHost, slaveHost, masterPorts, slavePorts);
        if(result){
            logger.info("deploy cluster sucess");
        }
        else{
            logger.error("deploy cluster failed.");
        }
    }

    private static void testDeploySentinel(){
//        1.1 master 端口号：6340
//        1.2 slave 端口号:6341
//        1.3 sentinel 端口号: 26340、26341、26342
//        1.4 封装原生redis 提供configRewrite方法
        String masterHost = "192.168.211.131";
        int masterPort = 6340;
        String[] slaveHosts = {"192.168.211.131"};
        int slavePort = 6341;
        String[] sentinelIps = {"192.168.211.131"};
        int sentinelPorts[] = {26340, 26341, 26342};
        int type = ConstUtil.CACHE_REDIS_STANDALONE;
        List<WebJedis> webJedisList = new ArrayList<WebJedis>();
        int appid = 6340;
        boolean isSuccess = redisDeployCenter.deploySentinelModel(appid, masterHost, slaveHosts, type, sentinelIps, masterPort, slavePort, sentinelPorts, webJedisList);
        if(!isSuccess){
            logger.error("delpoySentinelModel failed");
        }
        else {
            logger.info("deploySentinelModel success");
        }
    }

    private static void transfer() throws InvalidProtocolBufferException {
        UDPServerObject_Pb.UDPServerObject.Builder builder = UDPServerObject_Pb.UDPServerObject.newBuilder();
//        builder.setSessionID(udpClientObject.getSessionID());
//        builder.setResult(ByteString.copyFrom(data));
        byte[] result = {1};
        builder.setResult(ByteString.copyFrom(result));
        builder.setResultCode(1);
        byte data[] = builder.build().toByteArray();
        ArrayUtil.printByteArray(data);

        UDPServerObject_Pb.UDPServerObject udpServerObject = UDPServerObject_Pb.UDPServerObject.parseFrom(data);
        System.out.println(udpServerObject.getResultCode());

    }

    public static void testConnectRedis(){
        Jedis jedis = new Jedis(ip, port);
//        jedis.set("name", "guanxiangfei1111");
        String value = jedis.get("name1111");
        long ttl = jedis.ttl("name");
        System.out.println("value = " + value);
        System.out.println("ttl = " + ttl);
    }

    public static void readFile(String filePath){
        List<String> lines = FileUtil.getFileContent(filePath);
        for(String line : lines){
            System.out.println(line);
        }
    }

    public static void testSentinelFailover(){
        String host = "192.168.211.131";
        for(int i = 0; i < 3; i++){
            int port = 26340 + i;
            String masterName = "sentinel-192.168.211.131-6340";
            redisDeployCenter.sentinelFailover(host, port, masterName);
        }
    }

    public static boolean deployRedisInstance(String host, int port, int type){
        return redisDeployCenter.deployRedisInstance(host, port, ConstUtil.CACHE_REDIS_STANDALONE);
    }

    public static void shutdownInstances(String password){
        String masterHost = "192.168.211.129";
        int masterPort = 6340;
        String[] slaveHosts = {"192.168.211.129"};
        int slavePort = 6341;
        String[] sentinelIps = {"192.168.211.129"};
        int sentinelPorts[] = {26340, 26341, 26342};

        RedisCenter redisCenter = new RedisCenterImpl();
        redisCenter.shutdownRedis(masterHost, masterPort, password);
        redisCenter.shutdownRedis(slaveHosts[0], slavePort, password);
        redisCenter.shutdownRedis(sentinelIps[0], sentinelPorts[0], "");
        redisCenter.shutdownRedis(sentinelIps[0], sentinelPorts[1], "");
        redisCenter.shutdownRedis(sentinelIps[0], sentinelPorts[2], "");

    }

    public static void shutdownInstance(String ip, int port, String password){
        redisDeployCenter.shutdown(ip, port, password);
    }

    public static void startRedisInstanceAtPort(String ip, int port, int type, String password){
        machineCenter.startProcessAtPort(ip, port, type, password);
    }

    public static void testAddSentinel(){
        String ip = "192.168.211.131";
        String password = "g6noupkwcqmo0ch6";
        int port = 26343;
        String masterName = "sentinel-192.168.211.131-6340";
        String masterHost = ip;
        int masterPort = 6341;
        redisDeployCenter.addSentinel(ip, port, masterName, password, masterHost, masterPort);
    }

    public static void testAddSlave(){
        int port = 6344;
        for(int i = 0; i < 1000; i++){
            String host = "192.168.211.131";
            port += i;
            String masterHost = host;
            int masterPort = 6340;
            String password = "0rhzn7glpkd8cada";
            redisDeployCenter.addSlave(host, port, masterHost, masterPort, password);
        }
    }
}
