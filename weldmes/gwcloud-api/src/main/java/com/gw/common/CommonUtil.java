package com.gw.common;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class CommonUtil {

    /**
     * 判断字符串不为空，不为空返回true
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return StringUtils.isNotBlank(str);
    }

    /**
     * 判断集合不为空，不为空返回true
     * @param list
     * @return
     */
    public static boolean isNotEmptyList(List<Object> list){
        if (null != list && list.size() > 0){
            return true;
        }
        return false;
    }

}
