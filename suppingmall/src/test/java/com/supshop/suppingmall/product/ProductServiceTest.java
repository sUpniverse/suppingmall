package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryFactory;
import com.supshop.suppingmall.user.User;
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

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductService productService;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;
    @Autowired CategoryFactory categoryFactory;

    @Test
    @Transactional
    public void createProduct() throws Exception {
        //given
        User examUser = userFactory.createAdmin("exam");
        Category examCategory = categoryFactory.createCategory("exam");

        Product product = productFactory.createProduct("맥북프로", examUser, examCategory);
        Set<String> urls = new HashSet<>();
        urls.add("/images/product/20200819/18/현대차.png");

        //when
        productService.createProduct(product,urls);
        Product product1 = productService.findProduct(product.getProductId());


        //then
        assertThat(product1.getName()).isEqualTo(product.getName());
        assertThat(product1.getContents()).isEqualTo("<img src=\"/images/product/"+product1.getProductId()+ File.separator+"현대차.png\">");
        assertThat(product1.getThumbnail()).isEqualTo("/images/product/"+product1.getProductId()+ File.separator+"맥북16.jpg");
    }
}