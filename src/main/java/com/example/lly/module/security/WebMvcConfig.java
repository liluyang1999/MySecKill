package com.example.lly.module.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login_page").setViewName("login");
//        registry.addViewController("/login/home").setViewName("/login/viewHome");
//        registry.addViewController("/login/seckill").setViewName("/login/viewSeckill");
//        registry.addViewController("/login").setViewName("/viewLogin");
//        registry.addViewController("/403").setViewName("/403error");
//        registry.addViewController("/404").setViewName("/404error");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
