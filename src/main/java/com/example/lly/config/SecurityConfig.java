package com.example.lly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity  //在安全配置类上使用，包含@EnbaleGlobalAuthentication(表明可以在类里面的方法里配置AuthenticationManagerBuilder)
@EnableGlobalMethodSecurity(prePostEnabled = true)  //可以将安全模块注解用于方法上
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/static", "/register").permitAll() //静态、注册页面不受保护，直接访问
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")  //有任意权限即可
                .antMatchers("/admin/**").access("hasRole('ADMIN')")  //拥有管理员权限才可访问
                .anyRequest().authenticated();
    }

    //BCrypt加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}



//        http.formLogin().usernameParameter("userName")
//                        .passwordParameter("userPassword")
//                        .loginPage("/admin/login").permitAll()
//                        .and().authorizeRequests().anyRequest().permitAll();