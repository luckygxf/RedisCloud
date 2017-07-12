package com.gxf.common.util;

import java.util.List;

/**
 * Created by 58 on 2017/7/12.
 */
public class ListUtil {

    /**
     * 判断列表是否为空
     * */
    public static boolean isEmpty(List list){
        return null == list || list.size() == 0;
    }
}
