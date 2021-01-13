package com.example.lly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Config {

    //设置浏览网页的地址
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("秒杀系统接口文档")
                                    .contact(new Contact("lly的学习笔记", "https://github.com/liluyang1999", "liluyang1999@qq.com"))
                                    .termsOfServiceUrl("https://www.baidu.com")
                                    .version("1.0")
                                    .build();
    }

    @Bean
    public Docket mySecKillApi() {    //以controller包为基准来生成API文档，路径不限制，生成SelectorBuilder后再把这个Docket给build出来
        return new Docket(DocumentationType.SWAGGER_2).groupName("MySecKill")
                                                        .apiInfo(this.apiInfo())
                                                        .select()
                                                        .paths(PathSelectors.any())   //路径
                                                        .apis(RequestHandlerSelectors.basePackage("com.example.lly.controller"))
                                                        .build();
    }


}
