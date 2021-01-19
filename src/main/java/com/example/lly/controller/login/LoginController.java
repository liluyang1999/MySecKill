package com.example.lly.controller.login;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.Product;
import com.example.lly.entity.rbac.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private BaseMapper<Product> baseMapper;

    @Autowired
    private UserMapper userMapper;

    private Product product = new Product(123,"雀巢奶昔");

    @GetMapping("/insert")
    public String testMyBatis() {
        baseMapper.insert(new Product(123, "雀巢奶昔"));
        return "插入成功!";
    }

    @GetMapping("/queryAll")
    public List<User> queryTest() {
         return userMapper.queryAll();
    }

    @GetMapping("/queryById")
    public User query() {
        return userMapper.queryById(1);
    }

    @GetMapping("/update")
    public int updateTest() {
       return baseMapper.update(new Product(123, "黑岩石族"));
    }

    @GetMapping("/delete")
    public int deleteTest() {
        return baseMapper.delete(123, Product.class);
    }

}
