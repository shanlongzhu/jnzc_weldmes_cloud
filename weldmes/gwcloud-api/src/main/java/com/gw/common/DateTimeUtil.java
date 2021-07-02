package com.gw.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime(){

        Date date = new Date();// 获取当前时间

        String time = sdf.format(date);
        return time;
    }
}
