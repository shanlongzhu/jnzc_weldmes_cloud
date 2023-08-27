package com.shth.das.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义表名处理器
 */
@Slf4j
public class MyTableNameHandler implements TableNameHandler {

    private static final ThreadLocal<String> TABLE_SUFFIX_DATA = new ThreadLocal<>();

    private static final List<String> tableList = new ArrayList<>();

    static {
        //设置已经分表的表名
        tableList.add("otcrtd");
        tableList.add("sxrtd");
    }

    /**
     * 设置表名后缀
     *
     * @param dateSuffix 格式：yyyyMMdd
     */
    public static void setData(String dateSuffix) {
        TABLE_SUFFIX_DATA.set(dateSuffix);
    }

    private static void removeData() {
        TABLE_SUFFIX_DATA.remove();
    }

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (!tableList.contains(tableName)) {
            return tableName;
        }
        String tableNameSuffix = TABLE_SUFFIX_DATA.get();
        if (StringUtils.isNotBlank(tableNameSuffix)) {
            removeData();
            return tableName + "_" + tableNameSuffix;
        }
        String customNowDate = DateTimeUtils.getCustomNowDate();
        return tableName + "_" + customNowDate;
    }

}
