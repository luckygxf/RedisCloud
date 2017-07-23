package com.gxf.util;

/**
 * Created by 58 on 2017/7/22.
 */
public class StringUtil{

    /**
     * 判断字符串为空
     * */
    public static boolean isEmpty(String string){
        return null == string || string.length() == 0;
    }

    /**
     * 判断字符串不为空
     * */
    public static boolean isNotEmpty(String string){
        return !isEmpty(string);
    }
}
