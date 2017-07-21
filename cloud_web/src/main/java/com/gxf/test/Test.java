package com.gxf.test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.gxf.common.util.ArrayUtil;
import com.gxf.common.util.ConstUtil;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.redis.redisImpl.RedisDeployCenterImpl;
import com.gxf.udp.proto.UDPServerObject_Pb;
import com.gxf.util.FileUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by 58 on 2017/7/11.
 */
public class Test {
    private static String ip = "192.168.211.129";
//    private static String ip = "127.0.0.1";
    private static int port = 6041;
    //TODO:这里可以考虑用spring注入
    private static RedisDeployCenter redisDeployCenter = new RedisDeployCenterImpl();

    public static void main(String[] args) throws InvalidProtocolBufferException {

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
        jedis.set("name", "guanxiangfei");
        String value = jedis.get("name");
        System.out.println("value = " + value);
    }

    public static void readFile(String filePath){
        List<String> lines = FileUtil.getFileContent(filePath);
        for(String line : lines){
            System.out.println(line);
        }
    }

    public static boolean deployRedisInstance(String host, int port, int type){
        return redisDeployCenter.deployRedisInstance(host, port, ConstUtil.CACHE_REDIS_STANDALONE);
    }
}
