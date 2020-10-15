package com.supshop.suppingmall.category;

import com.supshop.suppingmall.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    // 기본적인 카테고리 가져오기
    @Cacheable(value = "SEARCH_CATEGORY_BY_ID", key = "#id")
    public Category getCategory(Long id) {
        return categoryMapper.findOne(id);
    }

    // id가 아닌 카테고리의 영문명으로 카테고리 정보 가져오기
    @Cacheable(value = "SEARCH_CATEGORY_BY_ENNAME", key = "#enName")
    public Category getCategoryByEnName(String enName) { return categoryMapper.findOneByEnName(enName); }

    // 자기부터 자식, 손자 카테고리까지 정보 가져오기
    @Cacheable(value = "SEARCH_CATEGORY_BY_GRANDPARENT", key = "#id")
    public Category getCategoryToGrandChildren(Long id) { return categoryMapper.findOneToGrandChildren(id); }

    // 최상단 카테고리부터 손자 카테고리까지 정보 가져오기
    @Cacheable(value = "SEARCH_CATEGORY_BY_TOP")
    public List<Category> getCategories() {
        return categoryMapper.findAllByTop();
    }

    // 자기부터 부모, 조부모 까지 가져오기
    @Cacheable(value = "SEARCH_CATEGORY_BY_GRANDCHILDREN", key = "#id")
    public Category getGrandParentByGrandChildren(Long id) {
        return categoryMapper.findGrandParentByGrandChildren(id);
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
