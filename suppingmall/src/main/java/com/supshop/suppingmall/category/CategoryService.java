package com.supshop.suppingmall.category;

import com.supshop.suppingmall.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public Category getCategory(Long id) {
        return categoryMapper.findOne(id);
    }
}
