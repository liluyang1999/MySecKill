package com.example.lly.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.ResourceBundle;

//CDN Configuration
//@Component
//public class InitConfig implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        WebApplicationContext webApplicationContext = (WebApplicationContext) contextRefreshedEvent.getApplicationContext();
//        ServletContext servletContext = webApplicationContext.getServletContext();
//        ResourceBundle resource = ResourceBundle.getBundle("cdn");
//        if(servletContext != null && resource != null) {
//            for (String key : resource.keySet()) {     //keySet()方法表明不会为空
//                servletContext.setAttribute(key, resource.getString(key));
//            }
//        }
//    }
//
//
//}
