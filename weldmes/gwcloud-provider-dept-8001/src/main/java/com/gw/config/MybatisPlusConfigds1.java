package com.gw.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Mybatis-plus 主数据源ds1配置（基础业务数据库：jnzc）
 * 多数据源配置依赖数据源配置
 */
@Configuration
@MapperScans(
        value = {
                @MapperScan(basePackages = MybatisPlusConfigds1.COLLECTION, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.WELDER, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.CRAFT, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.DISPATCH, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.SOLDERER, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.SYSDEPT, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.ARTIFACT, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.DEVICE, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.PERSON, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.PRODUCTIONTASK, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.TEAM, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.WELDTASK, sqlSessionFactoryRef = "ds1SqlSessionFactory"),
                @MapperScan(basePackages = MybatisPlusConfigds1.DEPT, sqlSessionFactoryRef = "ds1SqlSessionFactory")
        }
)
public class MybatisPlusConfigds1 {

    static final String COLLECTION = "com.gw.equipment.collection.dao";
    static final String WELDER = "com.gw.equipment.welder.dao";
    static final String CRAFT = "com.gw.process.craft.dao";
    static final String DISPATCH = "com.gw.process.dispatch.dao";
    static final String SOLDERER = "com.gw.process.solderer.dao";
    static final String SYSDEPT = "com.gw.sys.dao";
    static final String ARTIFACT = "com.gw.data.artifact.dao";
    static final String DEVICE = "com.gw.data.device.dao";
    static final String PERSON = "com.gw.data.person.dao";
    static final String PRODUCTIONTASK = "com.gw.data.person.dao";
    static final String TEAM = "com.gw.data.team.dao";
    static final String WELDTASK = "com.gw.data.weldTask.dao";
    static final String DEPT = "com.gw.dao";

    //主数据源 ds1数据源
    @Primary
    @Bean("ds1SqlSessionFactory")
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("ds1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        // 配置打印sql语句
        configuration.setLogImpl(StdOutImpl.class);
        //开启驼峰功能
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setGlobalConfig(new GlobalConfig().setBanner(false));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "ds1TransactionManager")
    public DataSourceTransactionManager ds1TransactionManager(@Qualifier("ds1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "ds1SqlSessionTemplate")
    public SqlSessionTemplate ds1SqlSessionTemplate(@Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
