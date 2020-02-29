package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductService productService;

    private User addUser() {

        return User.builder()
                .userId(1l)
                .build();
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        //given
        Category category = Category.builder().id(3l).build();

        ProductDetail productDetail = ProductDetail.builder()
                                            .detailId(1)
                                            .manufacturer("apple")
                                            .origin("베트남")
                                            .asNumber("02-333-4444")
                                            .spec1("256gb ssd")
                                            .spec2("16gb 메모리")
                                            .spec3("16인치 디스플레이")
                                            .spec4("트랙패드")
                                            .spec5("usb c type port 4개")
                                            .build();


        List<ProductOption> productOptions = new ArrayList<>();
        ProductOption productOption1 = new ProductOption();
        productOption1.setOptionId(1);
        productOption1.setOptionName("256");
        productOption1.setPrice(0);
        productOption1.setQuantity(50);
        ProductOption productOption2 = new ProductOption();
        productOption2.setOptionId(2);
        productOption2.setOptionName("512");
        productOption2.setPrice(300000);
        productOption2.setQuantity(50);
        productOptions.add(productOption1);
        productOptions.add(productOption2);


        User user = addUser();

        Product product = Product.builder()
                .category(category)
                .name("맥북프로")
                .price(2000000)
                .detail(productDetail)
                .options(productOptions)
                .seller(user)
                .build();



        //when
        productService.createProduct(product);
        Product product1 = productService.retrieveProduct(product.getProductId());

        //then
        assertThat(product1.getName()).isEqualTo(product.getName());
    }
}