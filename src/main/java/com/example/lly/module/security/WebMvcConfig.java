package com.example.lly.module.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("public_seckill_list");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/login_page").setViewName("login");
        registry.addViewController("/register_page").setViewName("register");
        registry.addViewController("/seckill_list_page").setViewName("public_seckill_list");
        registry.addViewController("/login_page/home_page").setViewName("home");
        registry.addViewController("/login_page/home_page/seckill_management_page").setViewName("system_management");
        registry.addViewController("/login_page/home_page/seckill_execution_page").setViewName("seckill_execution");
        registry.addViewController("/login_page/home_page/seckill_list_page").setViewName("private_seckill_list");
        registry.addViewController("/failure_page").setViewName("failure");
        registry.addViewController("/test").setViewName("test");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
