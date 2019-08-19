package com.supshop.suppingmall.product;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/products")
@RestController
public class ProductController {

    @GetMapping("")
    public String getProduct(@PathVariable String id) {
        return "";
    }

    @PostMapping("")
    public String createProduct(Product product) {
        return "";
    }

    @PutMapping("")
    public String updateProduct(@PathVariable String id, Product product) {
        return "";
    }

    @DeleteMapping("")
    public String deleteProduct(@PathVariable String id) {
        return "";
    }
}
