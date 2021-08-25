package com.shth.das.codeparam;

/**
 * @description: 分表策略枚举类
 * @author: Shan Long
 * @create: 2021-08-19
 */
public enum TableStrategyEnum {

    /**
     * 按年分表
     */
    YEAR("year"),

    /**
     * 按月分表
     */
    MONTH("month"),

    /**
     * 按周分表
     */
    WEEK("week"),

    /**
     * 按天分表
     */
    DAY("day"),

    /**
     * 自定义分表策略
     */
    CUSTOM("custom");

    private final String strategy;

    TableStrategyEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return this.strategy;
    }

}
