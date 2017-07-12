package com.gxf.client;

import redis.clients.jedis.Jedis;

/**
 * Created by 58 on 2017/7/10.
 */
public class ConnectRedisServer {
    private static String host = "192.168.211.129";
    private static int port = 6379;
    private static Jedis jedis;

    static {
        jedis = new Jedis(host, port);
    }

    public static void main(String[] args) {
        jedis.set("name", "guanxiangfei");
        String value = jedis.get("name");
        System.out.println("value = " + value);
    }

    private Jedis getJedis(){
        jedis = new Jedis(host, port);
        return jedis;
    }

    private String getKey(String key){
        return jedis.get(key);
    }

    private void set(String key, String value){
        jedis.set(key, value);
    }
}
