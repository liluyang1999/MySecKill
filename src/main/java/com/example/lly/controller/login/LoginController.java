package com.example.lly.controller.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @GetMapping(value = "/firstblood")
    public String firstBlood() {
        System.out.println("Hello World~");
        return "Hello World~";
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
