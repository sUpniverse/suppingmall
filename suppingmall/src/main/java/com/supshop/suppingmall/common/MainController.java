package com.supshop.suppingmall.common;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("")
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;

    @RequestMapping("")
    public String mainPage(Model model){
        List<Product> products = productService.findAllProductOnSale();
        model.addAttribute("products",products);
        return "main.html";
    }

    @RequestMapping("/test")
    public String testPage(){
        return "test.html";
    }
}
