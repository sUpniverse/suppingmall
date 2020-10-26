package com.supshop.suppingmall.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class CategoryFactory {

    @Autowired CategoryService categoryService;

    public Category buildCategory(String categoryName) {
        Category category = Category.builder().name(categoryName).enName(categoryName).build();
        return category;
    }

    public Category createCategory(String categoryName) {
        Category category = Category.builder()
                                    .name(categoryName)
                                    .enName(categoryName)
                                    .memo("좋아요!!"+categoryName)
                                    .build();
        categoryService.createCategory(category);
        return category;
    }
}
