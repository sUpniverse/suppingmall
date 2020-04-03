package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.category.Category;

import java.util.List;

public interface CategoryMapper {

    Category findOne(Long id);

    List<Category> findAll();

    List<Category> findAllByTop();
}
