package com.gw.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取系统当前时间
     */
    public static String getCurrentTime(){

        Date date = new Date();// 获取当前时间

        String time = sdf.format(date);
        return time;
    }

    /**
     * 获取系统次日当前时间
     */
    public static String getTomorrowTime(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();

        String time = DateTimeUtil.sdf.format(date);

        return time;
    }

    /**
     * @Date 2021/8/26 12:27
     * @Description 时间格式转化
     * @Params
     */
    public static String getRightTimeFormat(String time){

        Date date =new Date(time);

        String timeFormat = sdf.format(date);

        return timeFormat;
    }
}
