package com.shth.das.codeparam;

/**
 * 分表策略的执行时间
 */
public class TableExecuteEnum {

    /**
     * 按年分表（每月底23点执行）：0 0 23 L * ?
     */
    public static final String YEAR_TIME = "0 0 23 L * ?";

    /**
     * 按月分表（每月底23点执行）：0 0 23 L * ?
     */
    public static final String MONTH_TIME = "0 0 23 L * ?";

    /**
     * 按周分表（每周日23点执行）：0 0 23 ? * SUN
     */
    public static final String WEEK_TIME = "0 0 23 ? * SUN";

    /**
     * 按天分表（每天23点执行）：0 0 23 * * ?
     */
    public static final String DAY_TIME = "0 0 23 * * ?";

}
