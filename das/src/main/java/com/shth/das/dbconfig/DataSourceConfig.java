package com.shth.das.dbconfig;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 多数据源配置
 */
@Configuration
public class DataSourceConfig {

    //主数据源配置 ds1数据源
    @Primary
    @Bean(name = "ds1DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds1")
    public DataSourceProperties ds1DataSourceProperties() {
        return new DataSourceProperties();
    }

    //第二个ds2数据源配置
    @Bean(name = "ds2DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSourceProperties ds2DataSourceProperties() {
        return new DataSourceProperties();
    }

    //主数据源 ds1数据源
    @Primary
    @Bean(name = "ds1DataSource")
    public DataSource ds1DataSource(@Qualifier("ds1DataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource build = (DruidDataSource) dataSourceProperties.initializeDataSourceBuilder().build();
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(logFilter());
        build.setProxyFilters(filters);
        return build;
    }

    //第二个ds2数据源
    @Bean("ds2DataSource")
    public DataSource ds2DataSource(@Qualifier("ds2DataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource build = (DruidDataSource) dataSourceProperties.initializeDataSourceBuilder().build();
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(logFilter());
        build.setProxyFilters(filters);
        return build;
    }

    /**
     * JdbcTemplate主数据源ds1数据源[基础业务]
     */
    @Primary
    @Bean(name = "ds1JdbcTemplate")
    public JdbcTemplate ds1JdbcTemplate(@Qualifier("ds1DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * JdbcTemplate第二个ds2数据源[存实时数据]
     */
    @Bean(name = "ds2JdbcTemplate")
    public JdbcTemplate ds2JdbcTemplate(@Qualifier("ds2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * @description 配置慢sql拦截器
     * @return
     */
    @Bean(name = "statFilter")
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        //慢sql时间设置,即执行时间大于2000毫秒的都是慢sql
        statFilter.setSlowSqlMillis(2000);
        statFilter.setLogSlowSql(true);
        //statFilter.setMergeSql(true);
        return statFilter;
    }
    /**
     * @description 配置日志拦截器
     * @return
     */
    @Bean(name = "logFilter")
    public Slf4jLogFilter logFilter(){
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(true);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }
}
