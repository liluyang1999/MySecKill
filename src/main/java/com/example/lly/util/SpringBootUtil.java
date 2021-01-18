package com.example.lly.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBootUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringBootUtil.applicationContext == null) {
            SpringBootUtil.applicationContext = applicationContext;
        }
        System.out.println("****************ApplicationContext设置完成！**************************************");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBeanByApplicationContext(String name) {
        return getApplicationContext().getBean(name);
    }


    public static <T>  T getBeanByApplicationContext(Class<T> requiredType) {
        return getApplicationContext().getBean(requiredType);
    }

    public static <T> T getBeanByApplicationContext(String name, Class<T> requiredType) {
        return getApplicationContext().getBean(name, requiredType);
    }

}
