package com.gxf.test;

import redis.clients.jedis.Jedis;

/**
 * Created by 58 on 2017/7/21.
 */
public class Test {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.set("name", "guanxiangfei");
    }
}
