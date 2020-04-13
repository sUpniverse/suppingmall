package com.supshop.suppingmall.category;

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
        model.addAttribute("categories",categoryService.getCategories());
        return "/category/list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Category> getCategories(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/children")
    @ResponseBody
    public ResponseEntity<List<Category>> getChildCategories(@PathVariable Long id) {
        List<Category> category = categoryService.getChildByParent(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity createCategory(@RequestBody Category category) {
        Long categoryId = categoryService.saveCategory(category);
        URI uri = linkTo(CategoryController.class).slash(categoryId).toUri();
        return ResponseEntity.created(uri).build();
    }
}
