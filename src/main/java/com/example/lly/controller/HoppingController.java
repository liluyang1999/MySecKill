package com.example.lly.controller;

import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.SeckillService;
import com.example.lly.service.UserSecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private UserSecurityService userSecurityService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    @RequestMapping(value = {"/login_page", "/login"})
    public ModelAndView goToLoginPage() {
        return new ModelAndView("login");
    }


    @RequestMapping(value = {"/register_page", "/register"})
    public ModelAndView goToRegisterPage() {
        return new ModelAndView("register");
    }


    @RequestMapping(value = {"/login_page/home_page", "/login/home"}, method = RequestMethod.POST)
    public ModelAndView goToHomePage(HttpServletRequest request) {
        String token = request.getParameter("authorization");
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav = new ModelAndView();
        if (!userSecurityService.containsUserRole(user.getRoles())) {
            mav.setViewName("seckill_management");
            mav.addObject("hasUserRole", false);
        } else {
            mav.setViewName("home");
            mav.addObject("hasUserRole", true);
            List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
            List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
            mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
            mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        }

        if (userSecurityService.containsAdminRole(user.getRoles())) {
            mav.addObject("hasAdminRole", true);
        } else {
            mav.addObject("hasAdminRole", false);
        }

        mav.addObject("user", user);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    //公用seckill_list页面
    @RequestMapping({"/seckill_list_page", "/seckill_list"})
    public ModelAndView goToPublicSeckillListPage(HttpServletRequest request) {
        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        ModelAndView mav = new ModelAndView("public_seckill_list");
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        return mav;
    }


    //个人的seckill_list_page
    @RequestMapping(value = {"/login_page/seckill_list_page", "/login/seckill_list"}, method = RequestMethod.POST)
    public ModelAndView goToPrivateSeckillListPage(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("private_seckill_list");
        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("user", user);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    //秒杀详情下单
    @RequestMapping(value = {"/login_page/seckill_execution_page", "/login/seckill_execution"}, method = RequestMethod.POST)
    public ModelAndView goToExecuteSeckillPage(HttpServletRequest request, @RequestBody SeckillInfo seckillInfo) {
        String token = request.getParameter(JwtTokenUtil.TOKEN_HEADER);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("seckill_execution");
        mav.addObject("seckillInfo", seckillInfo);
        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("user", user);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    //管理秒杀活动, (新增活动), 获取秒杀活动列表
    @RequestMapping(value = {"/login_page/seckill_management_page", "/login/seckill_management"}, method = RequestMethod.POST)
    public ModelAndView goToManegeSeckillPage(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("seckill_management");
        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("user", user);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    @RequestMapping("/login_page/user_management")
    public ModelAndView goToUserManagementPage(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("user_management");
        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav.addObject("user", user);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    @RequestMapping(value = {"/login_page/myaccount_page", "/login/myaccount"}, method = RequestMethod.POST)
    public ModelAndView goToMyAccountPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav = new ModelAndView("myaccount");
        mav.addObject("user", user);

        if (userSecurityService.containsUserRole(user.getRoles())) {
            mav.addObject("hasUserRole", true);
        } else {
            mav.addObject("hasUserRole", false);
        }

        if (userSecurityService.containsAdminRole(user.getRoles())) {
            mav.addObject("hasAdminRole", true);
        } else {
            mav.addObject("hasAdminRole", false);
        }

        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    @RequestMapping(value = {"/login_page/myseckill_page", "/login/myseckill"}, method = RequestMethod.POST)
    public ModelAndView goToMySeckillPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        mav = new ModelAndView("myseckill");
        mav.addObject("user", user);

        if (userSecurityService.containsUserRole(user.getRoles())) {
            mav.addObject("hasUserRole", true);
        } else {
            mav.addObject("hasUserRole", false);
        }

        if (userSecurityService.containsAdminRole(user.getRoles())) {
            mav.addObject("hasAdminRole", true);
        } else {
            mav.addObject("hasAdminRole", false);
        }

        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    private boolean authenticateToken(String token) {
        return !StringUtils.isEmpty(token)
                && !jwtAuthService.validateExpiration(token)
                && jwtAuthService.validateUsername(token);
    }

}
