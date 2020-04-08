package com.supshop.suppingmall.category;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/category")
@Controller
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getAllCategories(HttpSession session, Model model) {
        model.addAttribute("categories",categoryService.getCategories());
        return "/category/list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Category> getCategories(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/child")
    @ResponseBody
    public ResponseEntity<List<Category>> getChildCategories(@PathVariable Long id) {
        List<Category> category = categoryService.getChildByParent(id);
        return ResponseEntity.ok(category);
    }
}
