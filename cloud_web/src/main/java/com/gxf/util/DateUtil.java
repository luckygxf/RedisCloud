package com.gxf.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 58 on 2017/7/17.
 */
public class DateUtil {

    /**
     * 将时间转换成yyyyMMddHHmm格式的字符串
     * */
    public static String formatYYYYMMddHHMM(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return simpleDateFormat.format(date);
    }
}
