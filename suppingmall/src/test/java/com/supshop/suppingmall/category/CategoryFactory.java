package com.supshop.suppingmall.category;

import com.supshop.suppingmall.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {

    @Autowired CategoryMapper categoryMapper;

    public Category createCategory(String categoryName) {
        Category category = Category.builder().name(categoryName).build();
        categoryMapper.save(category);
        return category;
    }
}
