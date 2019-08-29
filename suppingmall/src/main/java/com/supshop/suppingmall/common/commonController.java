package com.supshop.suppingmall.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("")
@Controller
public class commonController {

    @RequestMapping("")
    public String mainPage(){
        return "main.html";
    }
}
