package com.shth.das.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeanContext implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext = null;

    /**
     * 实现了ApplicationContextAware 接口，必须实现该方法；
     * 通过传递applicationContext参数初始化成员变量applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }

}
