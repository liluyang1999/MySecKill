package com.example.lly.controller;

import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HoppingController {

    //登录页面
    @RequestMapping("/login_page")
    public ModelAndView goToLoginPage() {
        return new ModelAndView("login");
    }

    //个人主页
    @RequestMapping("/login_page/home_page")
    public ModelAndView goToHomePage(HttpServletRequest request) {
        String userStr = request.getParameter("login_username");
        System.out.println("userStr + " + userStr);
        String userstr2 = (String) request.getSession().getAttribute("login_username");
        System.out.println("userStr2 + " + userstr2);
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("user", user);
        return mav;
    }

    //注册页面
    @RequestMapping("/register_page")
    public ModelAndView goToRegisterPage() {
        return new ModelAndView("register");
    }

    //秒杀活动列表
    @RequestMapping("/seckill_list_page")
    public ModelAndView goToSeckillListPage() {
        return new ModelAndView("seckill_list");
    }

    //秒杀下单列表
    @RequestMapping("/login_page/home_page/seckill_execution_page")
    public ModelAndView goToExecuteSeckillPage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        SeckillInfo seckillInfo = (SeckillInfo) request.getSession().getAttribute("seckillInfo");
        ModelAndView mav = new ModelAndView("seckill_execution");
        mav.addObject("user", user);
        mav.addObject("seckillInfo", seckillInfo);
        return mav;
    }

    //管理秒杀活动, (新增活动)
    @RequestMapping("/login_page/home_page/seckill_management_page")
    public String goToManegeSeckillPage() {
        return "seckill_management";
    }

}
