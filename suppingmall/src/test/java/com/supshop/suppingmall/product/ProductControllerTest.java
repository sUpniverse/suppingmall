package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    public void testPrdouct() throws Exception {
        //given
        Product product = new Product();
        Category category = Category.builder().id(3l).build();
        product.setCategory(category);
        product.setName("맥북프로");
        product.setPrice("2000000");

        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(1);
        productDetail.setManufacturer("apple");
        productDetail.setOrigin("베트남");
        productDetail.setAsNumber("02-333-4444");

        productDetail.setSpec1("256gb ssd");
        productDetail.setSpec2("16gb 메모리");
        productDetail.setSpec3("16인치 디스플레이");
        productDetail.setSpec4("트랙패드");
        productDetail.setSpec5("usb c type port 4개");
        product.setDetail(productDetail);

        List<ProductOption> productOptions = new ArrayList<>();
        ProductOption productOption1 = new ProductOption();
        productOption1.setId(1);
        productOption1.setOptionName("256");
        productOption1.setPrice("0");
        productOption1.setQuantity(50);
        ProductOption productOption2 = new ProductOption();
        productOption2.setId(2);
        productOption2.setOptionName("512");
        productOption2.setPrice("300000");
        productOption2.setQuantity(50);
        productOptions.add(productOption1);
        productOptions.add(productOption2);

        product.setOptions(productOptions);
        
        //when
        mockMvc.perform(post("/products")
                .requestAttr("product",product)
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andExpect(status().isOk());
        
        //then
    
    }
}