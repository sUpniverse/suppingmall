package com.supshop.suppingmall.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("")
@Controller
public class CommonController {

    @RequestMapping("")
    public String mainPage(){
        return "main.html";
    }

    @RequestMapping("/test")
    public String testPage(){
        return "test.html";
    }
}
