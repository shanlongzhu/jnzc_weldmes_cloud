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

    /**
     * 字符串长度拼接
     *
     * @param str    要拼接的字符串
     * @param length 要拼接的长度
     * @return 返回拼接好的字符串
     */
    public static String stringLengthJoint(String str, int length) {
        if (isNotEmpty(str)) {
            if (str.length() < length) {
                StringBuilder stringBuilder = new StringBuilder(str);
                for (int i = 0; i < (length - str.length()); i++) {
                    stringBuilder.insert(0, "0");
                }
                str = stringBuilder.toString();
            }
        }
        return str;
    }

}
