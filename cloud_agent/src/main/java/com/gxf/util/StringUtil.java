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
}
