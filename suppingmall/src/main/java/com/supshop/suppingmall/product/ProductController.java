package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.image.ImageController;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequestMapping("/products")
@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public ProductController(ProductService productService, CategoryService categoryService, ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @GetMapping("/form")
    public String form(HttpSession session,Model model) {
        User user = (User) session.getAttribute("user");
        if(user == null || !(user.getRole().equals(User.Role.SELLER) || user.getRole().equals(User.Role.MASTER))) {
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

    @PostMapping("")
    public String createProduct(Product product, MultipartFile[] thumnails, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null || !(user.getRole().equals(User.Role.SELLER) || user.getRole().equals(User.Role.MASTER))) {
            return "redirect:/users/loginform";
        }
        String thumnail = null;
        if(thumnails != null) {
            for(MultipartFile file : thumnails) {
                try {
                    thumnail = imageService.saveImage(file, ImageController.productSourceUrl,ImageController.productUri).toString();
                } catch (IOException e) {

                }
            }
            product.setThumbnail(thumnail);
        }
        productService.createProduct(product);
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
