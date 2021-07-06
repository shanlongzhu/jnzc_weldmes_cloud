package com.shth.das.util;

import java.util.List;

/**
 * 系统工具类
 */
public class CommonUtils {

    /**
     * 16进制字符串转10进制数字
     *
     * @param str:16进制的字符串
     * @param startByte:对应字节序号
     * @return
     */
    public static Integer dataAnalysis(String str, int[] startByte) {
        if (startByte.length > 0) {
            for (int byt : startByte) {
                if ((byt + 1) * 2 > str.length()) {
                    //数组下标越界，返回0，不处理
                    return 0;
                }
            }
            int length = startByte.length;
            int b = startByte[0];
            String substring = str.substring(b * 2, b * 2 + 2 * length);
            return Integer.valueOf(substring, 16);
        }
        return 0;
    }

    /**
     * 非空判断，非空返回true
     *
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return null != str && !"".equals(str);
    }

    /**
     * 非空判断，非空返回true
     *
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        return null != list && list.size() > 0;
    }

    /**
     * 字符串转化成为16进制字符串
     *
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return str.toString();
    }

    /**
     * 10进制字符串转16进制字符串
     *
     * @param str    10进制字符串
     * @param length 要拼接的长度
     * @return 拼接好的16进制字符串
     */
    public static String lengthJoint(String str, int length) {
        if (isNotEmpty(str)) {
            str = Integer.toHexString(Integer.parseInt(str));
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

    /**
     * 16进制字符串转10进制，再进行长度拼接
     *
     * @param str    16进制字符串
     * @param length 要拼接的长度
     * @return 10进制字符串
     */
    public static String hexToDecLengthJoint(String str, int length) {
        if (isNotEmpty(str)) {
            str = String.valueOf(Integer.parseInt(str, 16));
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

    /**
     * 16进制字符串转byte数组
     *
     * @param hex 16进制字符串
     * @return byte数组
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * ASCII码转16进制
     *
     * @param str ASCII码
     * @return 16进制
     */
    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString(aChar));
        }
        return hex.toString();
    }

    /**
     * 16进制转ASCII码
     *
     * @param hex 16进制
     * @return ASCII码
     */
    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    /**
     * 数组转16进制字符串
     *
     * @param bArray 数组
     * @return String
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
