package com.example.lly.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterceptorController {

    /**
     * 拦截Url直接访问并进行页面跳转
     */
    @RequestMapping("{url}.html")
    public String JumpToThePage(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.html")
    public String JumpToThePage(@PathVariable("module") String module, @PathVariable("url") String url) {
        return module + "/" + url;
    }



}
