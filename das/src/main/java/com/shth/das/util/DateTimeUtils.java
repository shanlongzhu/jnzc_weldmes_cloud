package com.shth.das.util;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateTimeUtils {

    public static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdfMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat getSdfDate = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat getHourDate = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
    public static final SimpleDateFormat getTodayTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    public static String format(String formatPattern, Date date) {
        return new SimpleDateFormat(formatPattern).format(date);
    }

    public static Date parse(String formatPattern, String dateString)
            throws ParseException {
        return new SimpleDateFormat(formatPattern).parse(dateString);
    }

    /**
     * 获取当前系统时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDateTime() {
        return sdfDateTime.format(System.currentTimeMillis());
    }

    public static String getNowDate() {
        return sdfDate.format(System.currentTimeMillis());
    }

    //日期转换时间戳 毫秒
    public static String dateToStamp(String s) throws Exception {
        Date date = sdfDateTime.parse(s);
        long time = date.getTime();
        return String.valueOf(time);
    }

    //计算相差多少小时
    public static int differHours(String startTime, String endTime) {
        int hour = 0;
        try {
            String startStamp = dateToStamp(startTime);//登记时间戳
            String endStamp = dateToStamp(endTime);//整改期限时间戳
            BigInteger startStampInt = BigInteger.valueOf(Long.parseLong(startStamp));
            BigInteger endStampInt = BigInteger.valueOf(Long.parseLong(endStamp));
            BigInteger apartStamp = endStampInt.subtract(startStampInt);
            BigInteger hourStamp = BigInteger.valueOf((long) (1000 * 60 * 60));
            BigInteger hours = apartStamp.divide(hourStamp);//相差几小时
            hour = hours.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 给当前时间增加小时
     * @param day 时间
     * @param hour 增加的小时
     * @return 返回增加后的时间
     */
    public static String addDateMinut(String day, int hour) {
        Date date = null;
        try {
            date = sdfDateTime.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return sdfDateTime.format(date);
    }

}
