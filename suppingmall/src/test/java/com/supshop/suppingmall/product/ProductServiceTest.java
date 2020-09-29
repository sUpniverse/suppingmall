package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.CategoryFactory;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Assert;
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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductService productService;
    @Autowired QnAService qnaService;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;
    @Autowired CategoryFactory categoryFactory;

    @Test
    @Transactional
    public void createProduct() throws Exception {
        //given
        User examUser = userFactory.createUser("exam");

        Product product = productFactory.buildProduct("맥북프로", examUser);
        Set<String> urls = new HashSet<>();
        urls.add("/images/product/20200819/18/현대차.png");

        //when
        productService.createProduct(product,urls);
        Product product1 = productService.getProduct(product.getProductId());


        //then
        assertThat(product1.getName()).isEqualTo(product.getName());
        assertThat(product1.getContents()).isEqualTo("<img src=\"/images/product/"+product1.getProductId()+ File.separator+"현대차.png\">");
        assertThat(product1.getThumbnail()).isEqualTo("/images/product/"+product1.getProductId()+ File.separator+"맥북16.jpg");
    }
    @Test
    @Transactional
    public void createQnA() throws Exception {
        //given
        int find = qnaService.getQnAList(null,null,null,null).size();

        User kevin = userFactory.createUser("kevin");
        User seller = userFactory.createSeller("seller");

        Product macBook = productFactory.createProduct("맥북", seller);


        QnA qna = QnA.builder().title("이거 좋나요?????")
                .creator(kevin)
                .product(macBook)
                .build();
        //when
        qnaService.createQnA(qna);

        //then
        Assert.assertEquals(find+1,qnaService.getQnAList(null,null,null,null).size());

    }

    @Test
    @Transactional
    public void createReplt() throws Exception {
        //given
        User kevin = userFactory.createUser("kevin");
        User seller = userFactory.createSeller("seller");

        Product macBook = productFactory.createProduct("맥북", seller);


        QnA qna = QnA.builder().title("이거 좋나요?????")
                .creator(kevin)
                .product(macBook)
                .build();

        QnA reply = QnA.builder().title("네 아주 좋습니다.")
                .creator(seller)
                .product(macBook)
                .build();

        //when
        qnaService.createQnA(qna);
        qnaService.createReply(qna.getQnaId(), reply);

        qna = qnaService.getQna(qna.getQnaId());

        //then
        Assert.assertEquals(qna.getReply().getQnaId(), reply.getQnaId());

    }

}