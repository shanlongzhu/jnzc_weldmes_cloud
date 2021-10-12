package com.gw.common;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 */
public class DateTimeUtils {

    public static final DateTimeFormatter DEFAULT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TODAY_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
    public static final DateTimeFormatter TODAY_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YEAR_DATETIME = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter HOUR_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
    public static final DateTimeFormatter CUSTOM_YEAR = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter CUSTOM_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter CUSTOM_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 获取当前系统时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 时间
     */
    public static String getNowDateTime() {
        return LocalDateTime.now().format(DEFAULT_DATETIME);
    }

    /**
     * 获取当前系统时间的第二天时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 时间
     */
    public static String getNowSecondDateTime() {
        return LocalDateTime.now().plusDays(1).format(DEFAULT_DATETIME);
    }

    /**
     * 时间格式化
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getDateTimeFormat(String dateTime) {
        return LocalDateTime.parse(dateTime).format(DEFAULT_DATETIME);
    }

    /**
     * 获取当前系统日期
     *
     * @return 时间
     */
    public static String getNowDate() {
        return LocalDate.now().format(TODAY_DATE);
    }

    /**
     * 获取当前系统日期或时间
     *
     * @param formatter 自定义格式
     * @return 时间
     */
    public static String getNowDate(DateTimeFormatter formatter) {
        return LocalDate.now().format(formatter);
    }

    /**
     * 获取当前时间的第二天日期
     *
     * @param formatter
     * @return
     */
    public static String getNowSecondDate(DateTimeFormatter formatter) {
        return LocalDate.now().plusDays(1).format(formatter);
    }

    //日期转换时间戳 毫秒
    public static String dateToStamp(String s) throws Exception {
        long time = LocalDateTime.parse(s, DEFAULT_DATETIME).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return String.valueOf(time);
    }

    //计算相差多少小时
    public static int differHours(String startTime, String endTime) {
        try {
            //登记时间戳
            String startStamp = dateToStamp(startTime);
            //整改期限时间戳
            String endStamp = dateToStamp(endTime);
            BigInteger startStampInt = BigInteger.valueOf(Long.parseLong(startStamp));
            BigInteger endStampInt = BigInteger.valueOf(Long.parseLong(endStamp));
            BigInteger apartStamp = endStampInt.subtract(startStampInt);
            BigInteger hourStamp = BigInteger.valueOf((1000 * 60 * 60));
            //相差几小时
            BigInteger hours = apartStamp.divide(hourStamp);
            return hours.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 给当前时间增加小时
     *
     * @param datetime 时间
     * @param hour     增加的小时
     * @return 返回增加后的时间
     */
    public static String addDateMinut(String datetime, int hour) {
        return LocalDateTime.parse(datetime, DEFAULT_DATETIME).plusHours(hour).format(DEFAULT_DATETIME);
    }

}
