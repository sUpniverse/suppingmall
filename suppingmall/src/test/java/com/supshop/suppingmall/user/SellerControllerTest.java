package com.supshop.suppingmall.user;

import com.supshop.suppingmall.user.Form.UpdateSellerForm;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SellerControllerTest {

    @Autowired private UserFactory userFactory;
    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private ModelMapper modelMapper;


    @Test
    @Transactional
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void sellerApply() throws Exception {
        //given
        User testUser = userFactory.createUser("test");
        StoreVO store = StoreVO.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("Y")
                .build();

        //when
        mockMvc.perform(post("/users/seller/{id}/apply",testUser.getUserId())
                .param("storeName",store.getStoreName())
                .param("storePrivateNumber",store.getStorePhoneNumber())
                .param("storeAddress",store.getStoreAddress())
                .param("storeAddressDetail",store.getStoreAddressDetail())
                .param("storeZipCode",store.getStoreZipCode())
                .param("storeApplyYn", store.getStoreApplyYn())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/"+testUser.getUserId()+"/form"))
                .andDo(print())
        ;

        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getSeller() throws Exception {
        //given
        User user = userFactory.createAdmin("admin");
        //when
        mockMvc.perform(get("/users/seller"))
                .andExpect(status().isOk());

        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getSellerApplyPage() throws Exception {
        //given
        User user = userFactory.createAdmin("test");

        //when
        mockMvc.perform(get("/users/seller/applicant")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/applySellerList"));

        //then

    }


    @Test
    @Transactional
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getApplicant() throws Exception {
        //given
        User applicant = userFactory.createApplicant("applicant");

        User admin = userFactory.createAdmin("admin");

        //when
        mockMvc.perform(get("/users/seller/apply")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/applySellerList"))
                .andExpect(model().attributeExists("userList"))
//                .andExpect(model().attribute("userList.storeVO",hasProperty("storeVO",hasProperty("storeName",is(applySeller.store.getStoreName())))))    이거 방법을 찾고 싶다.
        ;

        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void grantSellerRole () throws Exception {
        //given
        User applicant = userFactory.createApplicant("applicant");

        User admin = userFactory.createAdmin("admin");

        //when
        mockMvc.perform(patch("/users/seller/{id}/apply",applicant.getUserId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void updateSeller() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        StoreVO storeVO = seller.getStoreVO();

        storeVO.setStoreAddress("제주시");
        storeVO.setStoreAddressDetail("협재");
        storeVO.setStoreName("협재스토어");

        UpdateSellerForm form = modelMapper.map(storeVO, UpdateSellerForm.class);

        form.setPassword(UserFactory.userpassword);


        ObjectMapper objectMapper = new ObjectMapper();


        //when
        mockMvc.perform(put("/users/seller/{id}",seller.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(form))
                )
                .andDo(print())
                .andExpect(status().isOk());

        User user = userService.getUser(seller.getUserId());

        //then
        Assert.assertEquals(storeVO.getStoreAddress(),user.getStoreVO().getStoreAddress());
        Assert.assertEquals(storeVO.getStoreAddressDetail(),user.getStoreVO().getStoreAddressDetail());
        Assert.assertEquals(storeVO.getStoreName(),user.getStoreVO().getStoreName());

    }
}
