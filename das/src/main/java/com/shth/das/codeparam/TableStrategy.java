package com.shth.das.codeparam;

import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 分表策略
 * @author: Shan Long
 * @create: 2021-08-19
 */
@Slf4j
public class TableStrategy {

    /**
     * 按年分表（每月28日23点执行）：0 0 23 28 * ?
     * 按月分表（每月28日23点执行）：0 0 23 L * ?
     * 按周分表（每周日23点执行）：0 0 23 ? * SUN
     * 按天分表（每天23点执行）：0 0 23 * * ?
     */
    public static final String EXECUTE_TIME = TableExecuteEnum.DAY_TIME;

    /**
     * 指定使用的分表策略
     */
    private static final String TABLE_STRATEGY = TableStrategyEnum.DAY.getStrategy();

    /**
     * Map<K,Function<T,R>>
     * K:分表策略，T:输入时间点，R:返回当前表名
     */
    private static final Map<String, Function<String, String>> TIMING_TABLE_MAP = new ConcurrentHashMap<>();

    /**
     * Map<K,Function<T,R>>
     * K:分表策略，T:输入时间段，R:返回表名集合
     */
    private static final Map<String, Function<Map<String, String>, List<String>>> TIME_BUCKET_MAP = new ConcurrentHashMap<>();

    /**
     * Map<K,Function<T,R>>
     * K:分表策略，T:输入时间点，R:返回当前时间下一个表名
     */
    private static final Map<String, Function<String, String>> TIMING_NEXT_TABLE_MAP = new ConcurrentHashMap<>();

    /**
     * 表名后缀MAP
     */
    private static final Map<String, Function<String, String>> TABLE_NAME_SUFFIX_MAP = new ConcurrentHashMap<>();

    static {
        //时间点的当前表
        TIMING_TABLE_MAP.put(TableStrategyEnum.YEAR.getStrategy(), TableStrategy::getTableNameByYear);
        TIMING_TABLE_MAP.put(TableStrategyEnum.MONTH.getStrategy(), TableStrategy::getTableNameByMonth);
        TIMING_TABLE_MAP.put(TableStrategyEnum.WEEK.getStrategy(), TableStrategy::getTableNameByWeek);
        TIMING_TABLE_MAP.put(TableStrategyEnum.DAY.getStrategy(), TableStrategy::getTableNameByDay);
        //时间段的所有表集合
        TIME_BUCKET_MAP.put(TableStrategyEnum.YEAR.getStrategy(), TableStrategy::getTableNameByYear);
        TIME_BUCKET_MAP.put(TableStrategyEnum.MONTH.getStrategy(), TableStrategy::getTableNameByMonth);
        TIME_BUCKET_MAP.put(TableStrategyEnum.WEEK.getStrategy(), TableStrategy::getTableNameByWeek);
        TIME_BUCKET_MAP.put(TableStrategyEnum.DAY.getStrategy(), TableStrategy::getTableNameByDay);
        //时间点的下一个表
        TIMING_NEXT_TABLE_MAP.put(TableStrategyEnum.YEAR.getStrategy(), TableStrategy::getNextTableNameByYear);
        TIMING_NEXT_TABLE_MAP.put(TableStrategyEnum.MONTH.getStrategy(), TableStrategy::getNextTableNameByMonth);
        TIMING_NEXT_TABLE_MAP.put(TableStrategyEnum.WEEK.getStrategy(), TableStrategy::getNextTableNameByWeek);
        TIMING_NEXT_TABLE_MAP.put(TableStrategyEnum.DAY.getStrategy(), TableStrategy::getNextTableNameByDay);
        //存储表名后缀
        TABLE_NAME_SUFFIX_MAP.put(TableStrategyEnum.YEAR.getStrategy(), TableStrategy::getTableNameSuffixByYear);
        TABLE_NAME_SUFFIX_MAP.put(TableStrategyEnum.MONTH.getStrategy(), TableStrategy::getTableNameSuffixByMonth);
        TABLE_NAME_SUFFIX_MAP.put(TableStrategyEnum.WEEK.getStrategy(), TableStrategy::getTableNameSuffixByWeek);
        TABLE_NAME_SUFFIX_MAP.put(TableStrategyEnum.DAY.getStrategy(), TableStrategy::getTableNameSuffixByDay);
    }

    /**
     * 获取表名后缀
     *
     * @param dateTime 格式：yyyy-MM-dd HH:mm:ss
     * @return 表名后缀
     */
    public static String getTableNameSuffix(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        if (!TABLE_NAME_SUFFIX_MAP.containsKey(TABLE_STRATEGY)) {
            return null;
        }
        return TABLE_NAME_SUFFIX_MAP.get(TABLE_STRATEGY).apply(dateTime);
    }

    /**
     * 根据时间点获取当前OTC表名
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    public static String getTableNameByDateTime(TableNameEnum tableNameEnum, String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            if (TIMING_TABLE_MAP.containsKey(TABLE_STRATEGY)) {
                return tableNameEnum.getStrategy() + TIMING_TABLE_MAP.get(TABLE_STRATEGY).apply(dateTime);
            }
        }
        return null;
    }

    /**
     * 根据时间段获取OTC表名集合
     *
     * @param startTime yyyy-MM-dd HH:mm:ss
     * @param endTime   yyyy-MM-dd HH:mm:ss
     * @return List<String>
     */
    public static List<String> getTableNameByDateTime(TableNameEnum tableNameEnum, String startTime, String endTime) {
        if (CommonUtils.isNotEmpty(startTime) && CommonUtils.isNotEmpty(endTime)) {
            if (TIME_BUCKET_MAP.containsKey(TABLE_STRATEGY)) {
                Map<String, String> map = new HashMap<>();
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                List<String> otcTableList = TIME_BUCKET_MAP.get(TABLE_STRATEGY).apply(map);
                return otcTableList.stream().map(value -> tableNameEnum.getStrategy() + value).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 根据时间点获取下一个OTC表名
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    public static String getNextTableNameByDateTime(TableNameEnum tableNameEnum, String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            if (TIMING_NEXT_TABLE_MAP.containsKey(TABLE_STRATEGY)) {
                return tableNameEnum.getStrategy() + TIMING_NEXT_TABLE_MAP.get(TABLE_STRATEGY).apply(dateTime);
            }
        }
        return null;
    }

    /**
     * 根据时间点获取年份
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 年（yyyy）
     */
    private static String getTableNameByYear(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_YEAR);
        }
        return null;
    }

    /**
     * 根据时间段计算年份
     *
     * @param map 时间段map
     * @return 年份集合
     */
    private static List<String> getTableNameByYear(Map<String, String> map) {
        if (!map.isEmpty()) {
            try {
                if (map.containsKey("startTime") && map.containsKey("endTime")) {
                    LocalDateTime startTime = LocalDateTime.parse(map.get("startTime"), DateTimeUtils.DEFAULT_DATETIME);
                    int startYear = startTime.getYear();
                    int endYear = LocalDateTime.parse(map.get("endTime"), DateTimeUtils.DEFAULT_DATETIME).getYear();
                    //计算时间差，取绝对值
                    int totalYears = Math.abs(endYear - startYear);
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i <= totalYears; i++) {
                        list.add(startTime.plusYears(i).format(DateTimeUtils.CUSTOM_YEAR));
                    }
                    return list;
                }
            } catch (Exception e) {
                log.error("根据时间段计算年份异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 根据时间点获取月份
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 月（yyyyMM）
     */
    private static String getTableNameByMonth(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_MONTH);
        }
        return null;
    }

    /**
     * 根据时间段计算月份
     *
     * @param map 时间段map
     * @return 年月份集合
     */
    private static List<String> getTableNameByMonth(Map<String, String> map) {
        if (!map.isEmpty()) {
            try {
                if (map.containsKey("startTime") && map.containsKey("endTime")) {
                    final LocalDateTime startTime = LocalDateTime.parse(map.get("startTime"), DateTimeUtils.DEFAULT_DATETIME);
                    final LocalDateTime endTime = LocalDateTime.parse(map.get("endTime"), DateTimeUtils.DEFAULT_DATETIME);
                    final int startYear = startTime.getYear();
                    final int startMonth = startTime.getMonthValue();
                    final int endYear = endTime.getYear();
                    final int endMonth = endTime.getMonthValue();
                    //计算时间段相差几个月（绝对值）
                    final int total = Math.abs(endYear - startYear) * 12 + Math.abs(endMonth - startMonth);
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i <= total; i++) {
                        list.add(startTime.plusMonths(i).format(DateTimeUtils.CUSTOM_MONTH));
                    }
                    return list;
                }
            } catch (Exception e) {
                log.error("根据时间段计算月份异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 根据时间点获取周
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 周一（yyyyMMdd）
     */
    private static String getTableNameByWeek(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            try {
                final LocalDate parse = LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
                return LocalDateTime.of(parse, LocalTime.MIN).with(DayOfWeek.MONDAY).format(DateTimeUtils.CUSTOM_DAY);
            } catch (Exception e) {
                log.error("根据时间点获取周异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 根据时间段计算周数
     *
     * @param map 时间段map
     * @return 时间周集合
     */
    private static List<String> getTableNameByWeek(Map<String, String> map) {
        if (!map.isEmpty()) {
            try {
                if (map.containsKey("startTime") && map.containsKey("endTime")) {
                    final LocalDate startParse = LocalDate.parse(map.get("startTime"), DateTimeUtils.DEFAULT_DATETIME);
                    final LocalDate endParse = LocalDate.parse(map.get("endTime"), DateTimeUtils.DEFAULT_DATETIME);
                    //开始：周一
                    final LocalDateTime startMonday = LocalDateTime.of(startParse, LocalTime.MIN).with(DayOfWeek.MONDAY);
                    //结束：周一
                    final LocalDateTime endMonday = LocalDateTime.of(endParse, LocalTime.MIN).with(DayOfWeek.MONDAY);
                    //计算两个时间段相差周数
                    final long totalWeeks = Math.abs(Duration.between(startMonday, endMonday).toDays()) / 7;
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i <= totalWeeks; i++) {
                        list.add(startMonday.plusWeeks(i).format(DateTimeUtils.CUSTOM_DAY));
                    }
                    return list;
                }
            } catch (Exception e) {
                log.error("根据时间段计算周数异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 根据时间点获取天
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 当天（yyyyMMdd）
     */
    private static String getTableNameByDay(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return LocalDateTime.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_DAY);
        }
        return null;
    }

    /**
     * 根据时间段计算天数
     *
     * @param map 时间段map
     * @return 天数集合
     */
    private static List<String> getTableNameByDay(Map<String, String> map) {
        if (!map.isEmpty()) {
            try {
                if (map.containsKey("startTime") && map.containsKey("endTime")) {
                    final LocalDateTime startTime = LocalDateTime.parse(map.get("startTime"), DateTimeUtils.DEFAULT_DATETIME);
                    final LocalDateTime endTime = LocalDateTime.parse(map.get("endTime"), DateTimeUtils.DEFAULT_DATETIME);
                    //时间段相差天数
                    final long totalDays = Math.abs(Duration.between(startTime, endTime).toDays());
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i <= totalDays; i++) {
                        list.add(startTime.plusDays(i).format(DateTimeUtils.CUSTOM_DAY));
                    }
                    return list;
                }
            } catch (Exception e) {
                log.error("根据时间段计算天数异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取时间点的下一个年份
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    private static String getNextTableNameByYear(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            final LocalDateTime startTime = LocalDateTime.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
            return startTime.plusYears(1).format(DateTimeUtils.CUSTOM_YEAR);
        }
        return null;
    }

    /**
     * 获取时间点的下一个月份
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    private static String getNextTableNameByMonth(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            final LocalDateTime startTime = LocalDateTime.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
            return startTime.plusMonths(1).format(DateTimeUtils.CUSTOM_MONTH);
        }
        return null;
    }

    /**
     * 获取时间点的下一个周一
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    private static String getNextTableNameByWeek(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            final LocalDate startParse = LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
            final LocalDateTime startMonday = LocalDateTime.of(startParse, LocalTime.MIN).with(DayOfWeek.MONDAY);
            return startMonday.plusWeeks(1).format(DateTimeUtils.CUSTOM_DAY);
        }
        return null;
    }

    /**
     * 获取时间点的第二天
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名
     */
    private static String getNextTableNameByDay(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            final LocalDateTime startTime = LocalDateTime.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
            return startTime.plusDays(1).format(DateTimeUtils.CUSTOM_DAY);
        }
        return null;
    }

    /**
     * 根据时间获取表名后缀
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名后缀
     */
    private static String getTableNameSuffixByYear(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        return LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_YEAR);
    }

    /**
     * 根据时间获取表名后缀
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名后缀
     */
    private static String getTableNameSuffixByMonth(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        return LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_MONTH);
    }

    /**
     * 根据时间获取表名后缀
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名后缀
     */
    private static String getTableNameSuffixByWeek(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        LocalDate parse = LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME);
        return LocalDateTime.of(parse, LocalTime.MIN).with(DayOfWeek.MONDAY).format(DateTimeUtils.CUSTOM_DAY);
    }

    /**
     * 根据时间获取表名后缀
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return 表名后缀
     */
    private static String getTableNameSuffixByDay(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        return LocalDate.parse(dateTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.CUSTOM_DAY);
    }

}
