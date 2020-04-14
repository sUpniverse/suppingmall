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

    public List<Category> getChildByParent(Long id) {
        return categoryMapper.findChildByParent(id);
    }

    @Transactional
    public Long saveCategory(Category category) {
        categoryMapper.save(category);
        return category.getId();
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryMapper.delete(id);
    }

    @Transactional
    public void updateCategory(Long id,Category category) {
        categoryMapper.update(id,category);
    }
}
