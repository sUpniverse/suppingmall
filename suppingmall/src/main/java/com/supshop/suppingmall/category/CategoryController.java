package com.supshop.suppingmall.category;

import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/category")
@Controller
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getAllCategories(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/users/loginform";
        model.addAttribute("categories",categoryService.getCategories());
        return "/category/list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Category> getCategories(@PathVariable Long id,
                                                  HttpSession session,
                                                  Model model) {
        if (!isAdmin(session)) return ResponseEntity.badRequest().build();
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/children")
    @ResponseBody
    public ResponseEntity<List<Category>> getChildCategories(@PathVariable Long id,
                                                             HttpSession session,
                                                             Model model) {
        if (!isAdmin(session)) return ResponseEntity.badRequest().build();
        List<Category> category = categoryService.getChildByParent(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity createCategory(@RequestBody Category category,
                                         HttpSession session,
                                         Model model) {
        if (!isAdmin(session)) return ResponseEntity.badRequest().build();
        Long categoryId = categoryService.saveCategory(category);
        URI uri = linkTo(CategoryController.class).slash(categoryId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @RequestBody Category category,
                                                 HttpSession session,
                                                 Model model) {
        if (!isAdmin(session)) return ResponseEntity.badRequest().build();
        categoryService.updateCategory(id,category);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteCategory(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.badRequest().build();
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    private boolean isAdmin(HttpSession session) {
        if(SessionUtils.isSessionNull(session)) {
            return false;
        }
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        if (!sessionUser.getRole().equals(User.Role.MASTER) || !sessionUser.getRole().equals(User.Role.MASTER)) {
            return true;
        }
        return false;
    }
}
