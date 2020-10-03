package com.supshop.suppingmall.order;

import com.supshop.suppingmall.order.form.OrderForm;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired OrderFactory orderFactory;
    @Autowired UserFactory userFactory;

    // 임시 주문을 위한 요청
    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void createOrderSheet() throws Exception {
        User tester = userFactory.createUser("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/orders/orderSheet")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(tempOrderForm))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
        ;
    }

    // 임시 주문시 권한없는(Anonymous User) 요청 시 로그인 페이지로
    @Test
    public void createOrderSheet_Without_Authorities() throws Exception {
        User tester = userFactory.createAdmin("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/orders/orderSheet2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(tempOrderForm))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
        ;
    }

    // 임시 주문시 Csrf Token없이 요청 했을 경우
    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void createOrderSheet_Without_CsrfToken() throws Exception {
        User tester = userFactory.createAdmin("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/orders/orderSheet2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(tempOrderForm))
        )
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    // 임시 주문을 위한 요청
    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void getOrderSheet() throws Exception {

        //given
        User tester = userFactory.createAdmin("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/orders/orderSheet")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(tempOrderForm))
                .with(csrf()))
                .andReturn().getResponse();
        String redirectedUrl = response.getRedirectedUrl();

        //when

        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // 주문하기
    // Todo : form 형태 테스트 하기 어려움,,, REST 형태로 전환하길 고려
    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void orderTest() throws Exception {

        //given
        OrderForm orderForm = orderFactory.buildOrderForm();

        //when

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/order/success"))
                .andExpect(view().name("success"))
                .andDo(print());
    }
}