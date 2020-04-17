package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.image.ImageController;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequestMapping("/products")
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @GetMapping("/form")
    public String form(HttpSession session,Model model) {
        UserVO user = (UserVO) session.getAttribute("user");
        if(user == null || !(user.getRole().equals(User.Role.SELLER) || user.getRole().equals(User.Role.MASTER))) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("categories",categoryService.getCategory(2L).getChild());
        model.addAttribute("vendors", Delivery.DeliveryVendor.values());
        return "/product/form";
    }

    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.retrieveAllProduct();
        model.addAttribute("products",products);
        return "/product/list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.findProduct(id);
        model.addAttribute("product",product);
        return "/product/product";
    }

    @PostMapping("")
    public String createProduct(@Valid Product product, MultipartFile[] thumnails, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("user");
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

    @GetMapping("/seller")
    public String getProductsBySeller(HttpSession session,Model model) {
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        List<Product> products = productService.findProductsBySellerId(sessionUser.getUserId());
        model.addAttribute("user",sessionUser);
        model.addAttribute("products",products);
        return "/product/seller/list";
    }
}
