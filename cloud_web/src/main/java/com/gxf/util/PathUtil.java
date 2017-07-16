package com.gxf.util;

/**
 * Created by 58 on 2017/7/17.
 */
public class PathUtil {

    public static String getFilePath(String fileName){
        String path = PathUtil.class.getClassLoader().getResource(fileName).toString();

        return path.substring(6, path.length());
    }
}
