package com.example.lly.controller.login;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.dao.mapper.rbac.RoleMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.Product;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private BaseMapper<Product> baseMapper;

    @Autowired
    private DemoService demoService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    private Product product = new Product(123,"雀巢奶昔");

    @GetMapping("/queryUser")
    public User query1() {
         return userMapper.queryById(1);
    }

    @GetMapping("/queryRole")
    public Role query2() {
        ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return roleMapper.queryById(1);
    }

    @GetMapping("/queryPermission")
    public Permission query3() {
       return permissionMapper.queryById(11);
    }

    @RequestMapping("/find")
    public User findUser() {
        return demoService.findOne("zhangsan");
    }

    @RequestMapping("/findAnother")
    public User findAnother() {
        return demoService.findAnother("zhangsan");
    }


}
