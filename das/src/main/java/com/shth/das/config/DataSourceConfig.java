package com.shth.das.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.wall.WallConfig;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * 主数据源 ds1数据源
     */
    @Primary
    @Bean(name = "ds1DataSource")
    @ConfigurationProperties("spring.datasource.druid.ds1")
    public DataSource ds1DataSource() {
        DruidDataSource build = DruidDataSourceBuilder.create().build();
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(logFilter());
        build.setProxyFilters(filters);
        return build;
    }

    /**
     * 第二个ds2数据源
     */
    @Bean("ds2DataSource")
    @ConfigurationProperties("spring.datasource.druid.ds2")
    public DataSource ds2DataSource() {
        DruidDataSource build = DruidDataSourceBuilder.create().build();
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
     * 配置慢sql拦截器
     *
     * @return StatFilter
     */
    @Bean(name = "statFilter")
    public StatFilter statFilter() {
        //慢sql时间设置,即执行时间大于3000毫秒的都是慢sql
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setSlowSqlMillis(3000);
        return statFilter;
    }

    /**
     * 配置日志拦截器
     *
     * @return Slf4jLogFilter
     */
    @Bean(name = "logFilter")
    public Slf4jLogFilter logFilter() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(true);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }

    @Bean(name = "wallConfig")
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        //允许一次执行多条语句
        config.setMultiStatementAllow(true);
        return config;
    }

}
