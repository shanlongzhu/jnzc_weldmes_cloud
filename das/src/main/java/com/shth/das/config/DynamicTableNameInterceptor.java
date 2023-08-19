package com.shth.das.config;

import com.shth.das.codeparam.TableStrategy;
import com.shth.das.util.DateTimeUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DynamicTableNameInterceptor implements Interceptor {

    private static final String OTC_TABLE_NAME_PREFIX = "otcrtd";
    private static final String SX_TABLE_NAME_PREFIX = "sxrtd";

    /**
     * 拦截器：自定义表名并替换原来的表名
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        String sql = mappedStatement.getBoundSql(statementHandler.getParameterHandler().getParameterObject()).getSql();

        //表名替换
        sql = tableNameReplace(sql);

        metaObject.setValue("delegate.boundSql.sql", sql);
        return invocation.proceed();
    }

    /**
     * 自定义获取动态表名的方法
     *
     * @param originalTableName 原来的表名
     * @return 新的表名（根据当前时间计算）
     */
    private String getDynamicTableName(String originalTableName) {
        String nowDateTime = DateTimeUtils.getNowDateTime();
        // 根据业务逻辑获取动态表名
        if (OTC_TABLE_NAME_PREFIX.equals(originalTableName)) {
            return TableStrategy.getOtcTableByDateTime(nowDateTime);
        } else if (SX_TABLE_NAME_PREFIX.equals(originalTableName)) {
            return TableStrategy.getSxTableByDateTime(nowDateTime);
        } else {
            return originalTableName;
        }
    }

    /**
     * 表名替换
     *
     * @param sql 原来的SQL
     * @return 新的SQL
     */
    private String tableNameReplace(String sql) {
        if (sql.contains(OTC_TABLE_NAME_PREFIX)) {
            return sql.replace(OTC_TABLE_NAME_PREFIX, getDynamicTableName(OTC_TABLE_NAME_PREFIX));
        } else if (sql.contains(SX_TABLE_NAME_PREFIX)) {
            return sql.replace(SX_TABLE_NAME_PREFIX, getDynamicTableName(OTC_TABLE_NAME_PREFIX));
        } else {
            return sql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在此设置插件的属性
    }

}