package com.shth.das;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统启动类
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling //开启定时任务
@EnableTransactionManagement // 开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class DasApplication {

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
