package com.gxf.common.util;

/**
 * Created by 58 on 2017/7/12.
 */
public class ArrayUtil {

    /**
     * 输出字节数组内容
     * */
    public static void printByteArray(byte[] array){
        for(byte b : array){
            System.out.print(b + " ");
        }
        System.out.println();
    }
}
