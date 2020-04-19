package com.supshop.suppingmall.category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void getCategory() throws Exception {
        //given


        //when


        //then

    }


    @Test
    @Transactional
    public void saveCategory() throws Exception{
        //given
        Category category = buildCategory();

        //when
        Long categoryId = categoryService.saveCategory(category);
        Category newCategory = categoryService.getCategory(categoryId);

        //then
        assertEquals(category.getName(), newCategory.getName());
        assertEquals(category.getMemo(), newCategory.getMemo());
    }

    private Category buildCategory() {
        return Category.builder().name("청소기").build();
    }


}