package com.supshop.suppingmall.category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceTest {

    @Autowired private CategoryService categoryService;
    @Autowired private CategoryFactory categoryFactory;


    @Test
    public void saveCategory() throws Exception{
        //given
        Category category = categoryFactory.buildCategory("청소기");
        List<Category> originCategories = categoryService.getCategories();

        //when
        Long categoryId = categoryService.createCategory(category);
        Category newCategory = categoryService.getCategory(categoryId);
        List<Category> categories = categoryService.getCategories();

        //then
        assertEquals(originCategories.size()+1,categories.size());
    }

    @Test
    public void getCategory() throws Exception {
        //given
        Category test = categoryFactory.createCategory("test");

        //when
        Category newCategory = categoryService.getCategory(test.getId());

        //then
        assertEquals(test.getName(), newCategory.getName());
        assertEquals(test.getEnName(), newCategory.getEnName());
        assertEquals(test.getMemo(), newCategory.getMemo());

    }

    @Test
    public void getCategoryByEnName() throws Exception {
        //given
        Category test = categoryFactory.createCategory("test");

        //when
        Category newCategory = categoryService.getCategoryByEnName(test.getEnName());

        //then
        assertEquals(test.getName(), newCategory.getName());
        assertEquals(test.getEnName(), newCategory.getEnName());
        assertEquals(test.getMemo(), newCategory.getMemo());

    }

    @Test
    //테스트해보기 위해선, 먼저 mybatis의 <select>로 가서 cache기능을 꺼야한다.
    public void getCategoryByEnNameWithCache() throws Exception {
        //given
        for(int i = 0; i < 100; i++) {
            categoryFactory.createCategory("test"+i);
        }
        StopWatch stopWatch = new StopWatch("category에 redis cache 적용 후");
        //when
        stopWatch.start("1차");
        categoryService.getCategoryByEnName("test45");
        stopWatch.stop();
        stopWatch.start("2차");
        categoryService.getCategoryByEnName("test45");
        stopWatch.stop();
        stopWatch.start("3차");
        Category test45 = categoryService.getCategoryByEnName("test45");
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        //then
        assertEquals("test45",test45.getEnName());
        assertEquals("test45",test45.getName());
        assertEquals("좋아요!!test45",test45.getMemo());
        System.out.println(test45.toString());
    }



}