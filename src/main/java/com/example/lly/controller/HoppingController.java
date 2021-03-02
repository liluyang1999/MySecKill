package com.example.lly.controller;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.redis.RedisComponent;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.RolePermissionService;
import com.example.lly.service.SeckillService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.BaseUtil;
import com.example.lly.util.result.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HoppingController {

    @RequestMapping(value = {"/login_page", "/login"})
    public ModelAndView goToLoginPage() {
        return new ModelAndView("login");
    }


    @RequestMapping(value = {"/register_page", "/register"})
    public ModelAndView goToRegisterPage() {
        return new ModelAndView("register");
    }


    private final JwtAuthService jwtAuthService;
    private final SeckillService seckillService;
    private final UserSecurityService userSecurityService;
    private final PermissionMapper permissionMapper;
    private final RolePermissionService rolePermissionService;
    private final BaseUtil baseUtil;
    private final RedisComponent redisComponent;

    @Autowired
    public HoppingController(JwtAuthService jwtAuthService, SeckillService seckillService, UserSecurityService userSecurityService, PermissionMapper permissionMapper, RolePermissionService rolePermissionService, BaseUtil baseUtil, RedisComponent redisComponent) {
        this.jwtAuthService = jwtAuthService;
        this.seckillService = seckillService;
        this.userSecurityService = userSecurityService;
        this.permissionMapper = permissionMapper;
        this.rolePermissionService = rolePermissionService;
        this.baseUtil = baseUtil;
        this.redisComponent = redisComponent;
    }

    //公用seckill_list页面
    @RequestMapping({"/seckill_list_page", "/seckill_list", "/"})
    public ModelAndView goToPublicSeckillListPage(HttpServletRequest request) {
        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        ModelAndView mav = new ModelAndView("public_seckill_list");
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        return mav;
    }

    @RequestMapping({"/login_page/home_page", "/login/home"})
    public ModelAndView goToHomePage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav = new ModelAndView();
        if (!userSecurityService.containsUserRole(user.getRoles())) {
            mav.setViewName("system_management");
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

        mav.addObject("baseUtil", baseUtil);
        mav.addObject("user", user);
        mav.addObject("userSecurityService", userSecurityService);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }


    private boolean authenticateToken(String token) {
        return !StringUtils.isEmpty(token)
                && !jwtAuthService.validateExpiration(token)
                && jwtAuthService.validateUsername(token);
    }


    private void checkRole(User user, ModelAndView mav) {
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
    }

    private List<Timestamp> extractEndTime(List<SeckillInfo> seckillInfoList) {
        List<Timestamp> endTimeList = new ArrayList<>();
        for (SeckillInfo seckillInfo : seckillInfoList) {
            Timestamp endTime = seckillInfo.getEndTime();
            endTimeList.add(endTime);
        }
        return endTimeList;
    }

    //个人的seckill_list_page
    @RequestMapping({"/login_page/seckill_list_page", "/login/seckill_list"})
    public ModelAndView goToPrivateSeckillListPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("private_seckill_list");
        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        List<Product> productList = seckillService.getAllProduct();
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        mav.addObject("productList", productList);
        mav.addObject("baseUtil", baseUtil);

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav.addObject("user", user);
        mav.addObject("userSecurityService", userSecurityService);

        checkRole(user, mav);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    //秒杀详情下单
    @RequestMapping({"/login_page/seckill_execution_page", "/login/seckill_execution"})
    public ModelAndView goToExecuteSeckillPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        redisComponent.loadReadingCache();
        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav = new ModelAndView("seckill_execution");
        mav.addObject("user", user);
        mav.addObject("userSecurityService", userSecurityService);

        Integer seckillInfoId = Integer.valueOf(request.getParameter("seckillInfoId"));
        SeckillInfo seckillInfo = seckillService.getSeckillInfoById(seckillInfoId);
        mav.addObject("seckillInfo", seckillInfo);
        mav.addObject("startTime", BaseUtil.convertDateFormat(seckillInfo.getStartTime()));

        double percent = ((double) (seckillInfo.getExpectedNumber() - seckillInfo.getRemainingNumber())) / seckillInfo.getExpectedNumber() * 100;
        System.out.println("percent: " + percent);
        if (percent == 100.0) {
            mav.addObject("hasSoldOut", true);
        } else {
            mav.addObject("hasSoldOut", false);
        }
        mav.addObject("percent", percent + "%");

        boolean ifOrder = seckillService.hasOrderBefore(seckillInfoId, user.getId());
        if (ifOrder) {
            mav.addObject("ifOrder", true);
        } else {
            mav.addObject("ifOrder", false);
        }

        checkRole(user, mav);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    //商品下单
    @RequestMapping({"/login_page/product_execution_page", "/login/product_execution"})
    public ModelAndView goToProductPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        Integer productId = Integer.valueOf(request.getParameter("productId"));
        Product product = seckillService.getProductById(productId);

        mav = new ModelAndView("product_execution");
        mav.addObject("user", user);
        mav.addObject("userSecurityService", userSecurityService);
        mav.addObject("product", product);

        checkRole(user, mav);
        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    @RequestMapping({"/login_page/myaccount_page", "/login/myaccount"})
    public ModelAndView goToMyAccountPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav = new ModelAndView("myaccount");
        mav.addObject("user", user);

        checkRole(user, mav);

        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    @RequestMapping({"/login_page/myseckill_page", "/login/myseckill"})
    public ModelAndView goToMySeckillPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;

        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav = new ModelAndView("myseckill");
        mav.addObject("user", user);

        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
        mav.addObject("seckillInfoInProgressList", seckillInfoInProgressList);
        mav.addObject("seckillInfoInFutureList", seckillInfoInFutureList);
        mav.addObject("userSecurityService", userSecurityService);

        checkRole(user, mav);

        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    //管理秒杀活动, (新增活动), 获取秒杀活动列表
    @RequestMapping({"/login_page/system_management_page", "/login/system_management"})
    public ModelAndView goToManegeSeckillPage(HttpServletRequest request) {
        String token = request.getParameter(JwtTokenUtil.BODY_KEY);
        ModelAndView mav;
        if (!authenticateToken(token)) {
            mav = new ModelAndView("login");
            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
            return mav;
        }

        mav = new ModelAndView("system_management");
        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        user = rolePermissionService.addPermission(user);
        mav.addObject("user", user);
        checkRole(user, mav);

        List<User> userList = userSecurityService.getAllUsers();
        List<Product> productList = seckillService.getAllProduct();
        List<SeckillInfo> seckillInfoList = seckillService.getAllSeckillInfo();
        List<Permission> permissionList = permissionMapper.queryAll();
        mav.addObject("userList", userList);
        mav.addObject("productList", productList);
        mav.addObject("seckillInfoList", seckillInfoList);
        mav.addObject("permissionList", permissionList);
        mav.addObject("userSecurityService", userSecurityService);

        String newToken = jwtAuthService.refreshLogin(token);
        mav.addObject("token", newToken);
        return mav;
    }

    @RequestMapping("/getSeckillInfoList")
    @ResponseBody
    public ResponseResult<?> getUserList() {
        List<SeckillInfo> seckillInfoList = seckillService.getAllSeckillInfo();
        return ResponseResult.success(seckillInfoList);
    }
}


//    @RequestMapping("/login_page/user_management")
//    public ModelAndView goToUserManagementPage(HttpServletRequest request) {
//        String token = request.getHeader(JwtTokenUtil.BODY_KEY);
//        ModelAndView mav;
//        if (!authenticateToken(token)) {
//            mav = new ModelAndView("login");
//            mav.addObject("returnMsg", JwtTokenUtil.errorMsg);
//            return mav;
//        }
//
//        mav = new ModelAndView("user_management");
//        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
//        mav.addObject("user", user);
//        String newToken = jwtAuthService.refreshLogin(token);
//        mav.addObject( "token", newToken);
//        return mav;
//    }
