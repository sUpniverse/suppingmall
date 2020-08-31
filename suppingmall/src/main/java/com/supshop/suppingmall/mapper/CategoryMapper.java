package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.category.Category;

import java.util.List;

public interface CategoryMapper {

    Category findOne(Long id);
    Category findOneByEnName(String enName);
    List<Category> findAll();

    List<Category> findAllByTop();

    List<Category> findChildByParent(Long id);

    Category findGrandParentByGrandChildren(Long id);

    void save(Category category);

    void delete(Long id);

    //JPA 경우엔 save를 이용해서 하이버네이트가 dirty Checking을 통해 변화된 부분만 저장해주지만,, 우린 하이버네이트가 아니니깐
    void update(Long id,Category category);

}
