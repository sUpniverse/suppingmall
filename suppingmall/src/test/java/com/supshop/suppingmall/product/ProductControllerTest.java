package com.supshop.suppingmall.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.product.Form.*;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;

    private MockHttpSession session;


    private void addUserInSession(User user) {
        session = new MockHttpSession();
        session.setAttribute("user",user);

    }

    @Test
    @Transactional
    public void creatProduct() throws Exception {
        //given
        Category category = Category.builder().id(3l).build();

        DetailForm productDetail = DetailForm.builder()
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


        List<OptionForm> productOptions = new ArrayList<>();
        OptionForm productOption1 = new OptionForm();
        productOption1.setOptionId(1);
        productOption1.setOptionName("256");
        productOption1.setPrice(0);
        productOption1.setQuantity(50);
        OptionForm productOption2 = new OptionForm();
        productOption2.setOptionId(2);
        productOption2.setOptionName("512");
        productOption2.setPrice(300000);
        productOption2.setQuantity(50);
        productOptions.add(productOption1);
        productOptions.add(productOption2);


        User user = userFactory.createSeller("seller");
        addUserInSession(user);

        ProductForm product = ProductForm.builder()
                .category(category)
                .name("맥북프로")
                .price(2000000)
                .deliveryPrice(3000)
                .detail(productDetail)
                .options(productOptions)
                .seller(user)
                .build();

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("name","맥북프로");
        map.add("price","2000000");
        map.add("deliveryPrice","3000");

        //when
        mockMvc.perform(post("/products")
                .session(session)
                .params(map))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
        
        //then
    
    }


    @Test
    @Transactional
    public void testProductWrongAsNumber() throws Exception {
        //given
        Category category = Category.builder().id(3l).build();

        ProductDetail productDetail = ProductDetail.builder()
                .detailId(1)
                .manufacturer("apple")
                .origin("베트남")
                .asNumber("03333-333-4444")
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


        User user = userFactory.createSeller("seller");
        addUserInSession(user);

        Product product = Product.builder()
                .category(category)
                .name("맥북프로")
                .price(2000000)
                .deliveryPrice(3000)
                .detail(productDetail)
                .options(productOptions)
                .seller(user)
                .build();

        //when
        mockMvc.perform(post("/products")
                .session(session)
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(model().attribute("user",user))
                .andDo(print());

        //then

    }


    @Test
    @WithUserDetails(value = "suppingmall", userDetailsServiceBeanName = "userDetailsService")
    @Transactional
    public void createQna() throws Exception {
        //given
        User kevin = userFactory.createUser("suppingmall");
        User seller = userFactory.createSeller("seller");

        Product macBook = productFactory.createProduct("맥북", seller);


        QnaForm qnaForm = QnaForm.builder().title("이거 좋나요?????")
                                            .userId(kevin.getUserId())
                                            .build();

        //when

        mockMvc.perform(post("/products/{id}/qna",macBook.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(qnaForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());
        //then

    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    @Transactional
    public void createReply() throws Exception {
        //given
        QnA qna = productFactory.createQna();
        User seller = userFactory.createSeller("seller");

        QnaReplyForm reply = QnaReplyForm.builder()
                .title("네 아주 좋습니다.")
                .userId(seller.getUserId())
                .build();

        //when
        mockMvc.perform(post("/products/{productId}/qna/{qnaId}/reply", qna.getProduct().getProductId(), qna.getQnaId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply))
                        .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isCreated());


        //then

    }
}