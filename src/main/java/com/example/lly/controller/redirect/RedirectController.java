package com.example.lly.controller.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {


    @RequestMapping("/home.html")
    public String redirectToHome() {
        return "home";
    }


}
