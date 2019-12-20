package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.file.FileService;
import com.supshop.suppingmall.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/products")
@Controller
public class ProductController {

    private ProductService productService;
    private FileService fileService;
    private CategoryService categoryService;

    public ProductController(ProductService productService, FileService fileService, CategoryService categoryService) {
        this.productService = productService;
        this.fileService = fileService;
        this.categoryService = categoryService;
    }

    @GetMapping("/form")
    public String form(HttpSession session,Model model) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("categories",categoryService.getCategory(2L).getChild());
        return "/product/form";
    }

    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.retrieveAllProduct();
        model.addAttribute("products",products);
        return "/product/list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id) {
        return "";
    }

    // https://baekjungho.github.io/project-springboard15/ 참고 <-- commons libray
    // https://gofnrk.tistory.com/36 <-- spring boot
    // https://stackabuse.com/uploading-files-with-spring-boot/
    @PostMapping("")
    public String createProduct(Product product) {
//        Arrays.asList(files)
//                .stream()
//                .forEach(file -> {
//                    fileService.uploadFile(file,"product",product.getProductId());
//                });

//        productService.createProduct(product);
        return "forward:/";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, Product product) {
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return "";
    }
}
