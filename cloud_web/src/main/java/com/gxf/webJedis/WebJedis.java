package com.gxf.webJedis;

import redis.clients.jedis.Jedis;

/**
 * Created by 58 on 2017/7/17.
 */
public class WebJedis extends Jedis {
    public WebJedis(String host, int port){
        super(host, port);
    }

    public WebJedis(String host, int port, int timeOut){
        super(host, port, timeOut);
    }
}
