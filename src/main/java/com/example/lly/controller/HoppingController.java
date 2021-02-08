package com.example.lly.controller;

import com.example.lly.entity.SeckillInfo;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.SeckillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Controller
public class HoppingController {

    @Autowired
    private JwtAuthService jwtAuthService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @RequestMapping(value = {"/login_page", "/login"})
    public ModelAndView goToLoginPage() {
        return new ModelAndView("login");
    }


    //先进行token
    @RequestMapping(value = {"/login_page/home_page", "/login/home"})
    public ModelAndView goToHomePage(HttpServletRequest request) {
        String token = request.getParameter("authorization");
        ModelAndView mav;
        if (StringUtils.isEmpty(token) || !jwtAuthService.validateTokenFromHeader(token)) {
            mav = new ModelAndView("login");
            mav.addObject("ifAuthentictaion", false);
            return mav;
        }
        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        //ModelAndView mav;
        mav = new ModelAndView("home");
        mav.addObject("token", token);
        mav.addObject("username", JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("user", redisTemplate.opsForHash().get("users", JwtTokenUtil.getUsernameFromToken(token)));
        mav.addObject("ifAuthentication", true);
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        return mav;
    }


    @RequestMapping(value = {"/register_page", "/register"})
    public ModelAndView goToRegisterPage() {
        return new ModelAndView("register");
    }


    @RequestMapping(value = {"/seckill_list_page", "/seckill_list"})
    public ModelAndView goToSeckillListPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.TOKEN_HEADER);
        String username;
        ModelAndView mav = new ModelAndView("seckill_list");
        if (token != null) {
            username = JwtTokenUtil.getUsernameFromToken(token);
            mav.addObject("username", username);
            mav.addObject("hasLogin", true);
        } else {
            mav.addObject("hasLogin", false);
        }
        return mav;
    }


    //秒杀详情下单
    @RequestMapping(value = "/login_page/home_page/seckill_execution_page")
    public ModelAndView goToExecuteSeckillPage(HttpServletRequest request, @RequestBody SeckillInfo seckillInfo) {
        String token = request.getParameter(JwtTokenUtil.TOKEN_HEADER);
        ModelAndView mav;
        if (this.authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("ifAuthentictaion", false);
            return mav;
        }
        mav = new ModelAndView("seckill_execution");
        mav.addObject("token", token);
        mav.addObject("username", JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("seckillInfo", seckillInfo);
        mav.addObject("ifAuthentication", true);
        return mav;
    }


    //管理秒杀活动, (新增活动), 获取秒杀活动列表
    @RequestMapping("/login_page/home_page/seckill_management_page")
    public ModelAndView goToManegeSeckillPage(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        ModelAndView mav;
        if (this.authenticateToken(token)) {
            //验证不通过, 跳回登录页面
            mav = new ModelAndView("login");
            mav.addObject("ifAuthentictaion", false);
            return mav;
        }

        List<SeckillInfo> seckillInfoList = seckillService.getAllSeckillInfo();
        mav = new ModelAndView("seckill_management");
        mav.addObject("username", JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("seckillInfoList", seckillInfoList);
        mav.addObject("ifAuthentication", true);
        return mav;
    }


    private boolean authenticateToken(String token) {
        return !StringUtils.isEmpty(token) && jwtAuthService.validateTokenFromHeader(token);
    }

}
