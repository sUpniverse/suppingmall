package com.supshop.suppingmall.category;

import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryServiceTest {

    @Autowired private CategoryService categoryService;
    @Autowired private CategoryFactory categoryFactory;
    @Autowired private UserFactory userFactory;
    @Autowired private MockMvc mockMvc;

    @Test
    public void getCategoryPage() throws Exception {
        //given

        //when
        mockMvc.perform(get("/category/list")
                            .with(user(userFactory.createAdminToSession("kevin"))))
                .andExpect(status().isOk())
                .andDo(print())
        ;

        //then

    }


    @Test
    @Transactional
    public void saveCategory() throws Exception{
        //given
        Category category = categoryFactory.createCategory("청소기");

        //when
        Long categoryId = categoryService.saveCategory(category);
        Category newCategory = categoryService.getCategory(categoryId);

        //then
        assertEquals(category.getName(), newCategory.getName());
        assertEquals(category.getMemo(), newCategory.getMemo());
    }


}