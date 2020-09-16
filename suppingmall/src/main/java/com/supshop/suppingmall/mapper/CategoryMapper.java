package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.category.Category;

import java.util.List;

public interface CategoryMapper {

    //자신과 부모, 자식 까지 조회
    Category findOne(Long id);
    //자신과 부모, 자식, 손자 까지 조회
    Category findOneToGrandChildren(Long id);
    Category findOneByEnName(String enName);
    List<Category> findAll();

    //부모가 없는 최상단의 카테고리 목록 조회
    List<Category> findAllByTop();
    List<Category> findChildByParent(Long id);

    // 손자가 조부모의 카테고리까지 조회
    Category findGrandParentByGrandChildren(Long id);

    void save(Category category);

    void delete(Long id);

    //JPA 경우엔 save를 이용해서 하이버네이트가 dirty Checking을 통해 변화된 부분만 저장해주지만,, 우린 하이버네이트가 아니니깐
    void update(Long id,Category category);

}
