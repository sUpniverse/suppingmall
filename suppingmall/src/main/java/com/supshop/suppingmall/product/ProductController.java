package com.supshop.suppingmall.product;

import com.supshop.suppingmall.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/products")
@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    FileService fileService;

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

    // https://baekjungho.github.io/project-springboard15/ 참고 <-- commons libray
    // https://gofnrk.tistory.com/36 <-- spring boot
    // https://stackabuse.com/uploading-files-with-spring-boot/
    @PostMapping("")
    public String createProduct(Product product, HttpSession session, @RequestPart MultipartFile[] files) {

        Arrays.asList(files)
                .stream()
                .forEach(file -> {
                    fileService.uploadFile(file,"product",product.getProductId());
                });

        productService.createProduct(product);
        return "forward:/products/}";
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
