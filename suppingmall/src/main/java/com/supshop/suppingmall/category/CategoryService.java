package com.supshop.suppingmall.category;

import com.supshop.suppingmall.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public Category getCategory(Long id) {
        return categoryMapper.findOne(id);
    }

    public List<Category> getCategories() {
        return categoryMapper.findAllByTop();
    }
}
