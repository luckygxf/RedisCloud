package com.gxf;


import com.gxf.common.util.IPUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by 58 on 2017/7/19.
 */
public class Test {

    public static void main(String[] args) throws UnknownHostException {
        Map<String, String> map = new Hashtable<String, String>();
        map.put(null, "dddd");
        System.out.println(map.get(null));
    }
}
