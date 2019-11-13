package com.supshop.suppingmall.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/form")
    public String form() {
        return "/product/form";
    }

    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.retrieveAllProduct();
        model.addAttribute("products",products);
        return "/product/list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id) {
        return "";
    }

    @PostMapping("")
    public String createProduct(Product product) {
        return "";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable String id, Product product) {
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        return "";
    }
}
