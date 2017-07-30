package com.gxf.util;

import com.gxf.agent.constants.BaseConstant;

/**
 * Created by 58 on 2017/7/30.
 */
public class StringUtil {

    /**
     * 去掉字符串中空白的地方
     * */
    public static String trimToEmpty(String originalStr){
        if(null == originalStr || originalStr.length() == 0){
            return EmptyObjectConstant.EMPTY_STRING;
        }
        if(originalStr.endsWith(BaseConstant.WORD_SEPARATOR)){
            return originalStr;
        }
        return originalStr.trim();
    }

    /**
     * 判断字符串是否为空
     * */
    public static boolean isBlank(String... originalStraArray){
        if(null == originalStraArray || 0 == originalStraArray.length){
            return true;
        }
        for(String s : originalStraArray){
            if(!isEmpty(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     * */
    private static boolean isEmpty(String s){
        return s == null || s.length() == 0;
    }
}
