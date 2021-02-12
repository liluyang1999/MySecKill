package com.example.lly.module.security;

import com.example.lly.service.UserSecurityService;
import com.example.lly.util.encryption.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// /*是拦截所有文件夹但不包括子文件夹, /*是拦截所有文件夹包括其子文件夹
@Configuration
@EnableWebSecurity  //在安全配置类上使用，包含@EnbaleGlobalAuthentication(表明可以在类里面的方法里配置AuthenticationManagerBuilder)
@EnableGlobalMethodSecurity(prePostEnabled = true)  //可以将安全模块注解用于方法上
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public UserSecurityService userSecurityService;


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(WebSecurity web) {
        //忽略静态资源的验证
        web.ignoring().antMatchers("/static/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //stateless禁用session
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.authorizeRequests()
//                .antMatchers("/login_page/requestLogin").permitAll()
//                .antMatchers("/login_page/refreshLogin").permitAll()
//                .antMatchers("/register_page").permitAll()
//                .antMatchers("/seckill_list_page").permitAll()
//                .antMatchers("/login_page/home_page/seckill_management_page").hasRole("ADMIN")
//                .antMatchers("/login_page/home_page/seckill_execution_page").hasAnyRole("ADMIN", "USER")
//                .anyRequest().authenticated();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //UserSecuritService调用loadUserByUsername的时候会进行密码加密和比较
        builder.userDetailsService((UserDetailsService) userSecurityService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return MD5Util.encodeString(charSequence.toString());
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }


    public static void main(String[] args) {
        String str1 = "123456";
        System.out.println(MD5Util.encodeString(str1));
        System.out.println(")]\\-(/_UXU.-YU-..)YZ)\\Y[*^\\*TT_)");
    }


}



//四大模块 authorizeRequests, formLogin, logout, csrf
//        http.formLogin()
//                //配置登录界面
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginPage("/login_page").permitAll()    //无条件访问登录页面
//                .failureUrl("/login_page/failure_page")
//                .failureHandler(authenticationFailureHandler)
//                .defaultSuccessUrl("/login_page/home_page", true)
//                .successHandler(authenticationSuccessHandler)
//            .and()
//                //配置URL是否需要认证
//                .authorizeRequests()
//                .antMatchers("/login_page/requestLogin").permitAll()
//                .antMatchers("/login_page/refreshLogin").permitAll()
//                .antMatchers("/register_page").permitAll()
//                .antMatchers("/seckill_list_page").permitAll()
//                .antMatchers("/login_page/home_page/seckill_management_page").hasRole("ADMIN")
//                .antMatchers("/login_page/home_page/seckill_execution_page").hasAnyRole("ADMIN", "USER")
//                .anyRequest().authenticated()
//            .and().logout().permitAll()
//            .and().rememberMe().rememberMeParameter("rememberMe");
//        http.authorizeRequests().anyRequest().permitAll();

//    @Autowired
//    private AuthenticationSuccessHandler authenticationSuccessHandler;
//
//    @Autowired
//    private AuthenticationFailureHandler authenticationFailureHandler;
