package com.shth.das.codeparam;

/**
 * 表名枚举类
 */
public enum TableNameEnum {

    /**
     * OTC表名
     */
    OTC("otcrtd_"),

    /**
     * 松下表名
     */
    SX("sxrtd_");

    private final String strategy;

    TableNameEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return this.strategy;
    }

}
