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

    /* Todo : 카테고리의 변형은 거의 없음, 따라서 서비스 구동 시 HashMap에 미리 담아서 DB가 아닌, HasMap에서 주는건 어떨지..? 혹은 MemCache 나 redis에
       또, Create 혹은 Update가 발생시 다시 갱신만 해주면 되도록 구현
     */

    public Category getCategory(Long id) {
        return categoryMapper.findOne(id);
    }
    public Category getCategoryByEnName(String enName) {
        return categoryMapper.findOneByEnName(enName);
    }
    public Category getCategoryToGrandChildren(Long id) {
        return categoryMapper.findOneToGrandChildren(id);
    }

    public List<Category> getCategories() {
        return categoryMapper.findAllByTop();
    }

    public List<Category> getChildByParent(Long id) {
        return categoryMapper.findChildByParent(id);
    }

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
