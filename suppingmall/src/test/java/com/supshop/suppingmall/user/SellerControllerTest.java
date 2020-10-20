package com.supshop.suppingmall.user;

import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.PasswordCheckForm;
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
@Transactional
public class SellerControllerTest {

    @Autowired private UserFactory userFactory;
    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private ModelMapper modelMapper;

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getSellerMainPage() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        //when
        mockMvc.perform(get("/users/seller/{id}/main",seller.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/seller/main"))
                .andDo(print());
        //then

    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getSellerMainPage_실패_invalid_auth_user() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/seller/{id}/main",user.getUserId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
        //then

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getSellers() throws Exception {
        //given
        User user = userFactory.createAdmin("admin");
        //when
        mockMvc.perform(get("/users/seller"))
                .andExpect(status().isOk());

        //then

    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdateFormPage() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        //when
        mockMvc.perform(get("/users/seller/{id}/updateform",seller.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/seller/updateForm"))
                .andDo(print());
        //then
    }


    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void updateSeller() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        StoreVO storeVO = seller.getStoreVO();

        storeVO.setStoreAddress("제주시");
        storeVO.setStoreAddressDetail("협재");
        storeVO.setStoreName("협재스토어");
        storeVO.setStoreZipCode("000333");
        storeVO.setStorePhoneNumber("070-3334-5555");
        storeVO.setStorePrivateNumber("123374445");

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
        //then
    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void updateSeller_실패_wrong_password() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        StoreVO storeVO = seller.getStoreVO();

        storeVO.setStoreAddress("제주시");
        storeVO.setStoreAddressDetail("협재");
        storeVO.setStoreName("협재스토어");
        storeVO.setStoreZipCode("000333");
        storeVO.setStorePhoneNumber("070-3334-5555");
        storeVO.setStorePrivateNumber("123374445");

        UpdateSellerForm form = modelMapper.map(storeVO, UpdateSellerForm.class);

        form.setPassword(UserFactory.userpassword+1);


        ObjectMapper objectMapper = new ObjectMapper();


        //when
        mockMvc.perform(put("/users/seller/{id}",seller.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(form))
        )
                .andExpect(status().isBadRequest())
                .andDo(print());
        //then
    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void updateSeller_실패_valid_violation() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        StoreVO storeVO = seller.getStoreVO();

        storeVO.setStoreAddress("제주시");
        storeVO.setStoreAddressDetail("협재");
        storeVO.setStoreZipCode("000333");
        storeVO.setStorePhoneNumber("070-3334-5555");


        UpdateSellerForm form = modelMapper.map(storeVO, UpdateSellerForm.class);

        form.setPassword(UserFactory.userpassword+1);


        ObjectMapper objectMapper = new ObjectMapper();


        //when
        mockMvc.perform(put("/users/seller/{id}",seller.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(form))
        )
                .andExpect(status().isBadRequest())
                .andDo(print());
        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getApplySellerPage() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/seller/apply",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/seller/applySellerForm"))
                .andDo(print());
        //then

    }

    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getApplySellerPage_실패_invalid_auth() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        //when
        mockMvc.perform(get("/users/seller/apply",seller.getUserId()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/error/403"))
                .andDo(print());
        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getApplySellerPage_실패_already_applied() throws Exception {
        //given
        User user = userFactory.createUser("user");
        StoreVO y = StoreVO.builder().storeApplyYn("Y").build();
        user.setStoreVO(y);
        userService.patchUser(user.getUserId(), user);

        //when
        mockMvc.perform(get("/users/seller/apply",user.getUserId()))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("/error/400"))
                .andDo(print());
        //then

    }


    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void applySeller() throws Exception {
        //given
        User user = userFactory.createUser("user");
        ApplySellerForm form = ApplySellerForm.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storePhoneNumber("070-3334-5555")
                .storeApplyYn("Y")
                .build();

        //when
        mockMvc.perform(post("/users/seller/apply")
                .param("storeName",form.getStoreName())
                .param("storePrivateNumber",form.getStorePhoneNumber())
                .param("storePhoneNumber", form.getStorePhoneNumber())
                .param("storeAddress",form.getStoreAddress())
                .param("storeAddressDetail",form.getStoreAddressDetail())
                .param("storeZipCode",form.getStoreZipCode())
                .param("storeApplyYn", form.getStoreApplyYn())
                .with(csrf())

        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/"+user.getUserId()+"/form"))
                .andDo(print())
        ;
        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void applySeller_실패_invalid_user() throws Exception {
        //given
        User user = userFactory.createSeller("user");
        ApplySellerForm form = ApplySellerForm.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storePhoneNumber("070-3334-5555")
                .storeApplyYn("Y")
                .build();

        //when
        mockMvc.perform(post("/users/seller/apply")
                .param("storeName",form.getStoreName())
                .param("storePrivateNumber",form.getStorePhoneNumber())
                .param("storePhoneNumber", form.getStorePhoneNumber())
                .param("storeAddress",form.getStoreAddress())
                .param("storeAddressDetail",form.getStoreAddressDetail())
                .param("storeZipCode",form.getStoreZipCode())
                .param("storeApplyYn", form.getStoreApplyYn())
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/"+user.getUserId()+"/form"))
                .andDo(print())
        ;
        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void applySeller_실패_already_applied() throws Exception {
        //given
        User user = userFactory.createUser("user");
        StoreVO y = StoreVO.builder().storeApplyYn("Y").build();
        user.setStoreVO(y);
        userService.patchUser(user.getUserId(), user);

        ApplySellerForm form = ApplySellerForm.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storePhoneNumber("070-3334-5555")
                .storeApplyYn("Y")
                .build();

        //when
        mockMvc.perform(post("/users/seller/apply")
                .param("storeName",form.getStoreName())
                .param("storePrivateNumber",form.getStorePhoneNumber())
                .param("storePhoneNumber", form.getStorePhoneNumber())
                .param("storeAddress",form.getStoreAddress())
                .param("storeAddressDetail",form.getStoreAddressDetail())
                .param("storeZipCode",form.getStoreZipCode())
                .param("storeApplyYn", form.getStoreApplyYn())
                .with(csrf())
        )
                .andExpect(status().isBadRequest())
                .andExpect(view().name("/error/400"))
                .andDo(print())
        ;

    }


    @Test
    @Transactional
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getApplySellerListPage() throws Exception {
        //given
        User user = userFactory.createAdmin("admin");

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
    public void grantSellerRole () throws Exception {
        //given
        User applicant = userFactory.createApplicant("applicant");


        //when
        mockMvc.perform(patch("/users/seller/{id}/apply",applicant.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void grantSellerRole_실패_invalid_auth() throws Exception {
        //given
        User applicant = userFactory.createApplicant("applicant");


        //when
        mockMvc.perform(patch("/users/seller/{id}/apply",applicant.getUserId())
                .contentType(MediaType.APPLICATION_JSON)

        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/error/403"));

        //then

    }


    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void getTransferForm() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");


        //when
        mockMvc.perform(get("/users/seller/transfer",seller.getUserId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/seller/transferForm"));

        //then

    }


    @Test
    @Transactional
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "userDetailsService")
    public void transferSellerToUser() throws Exception {
        //given
        User seller = userFactory.createSeller("seller");

        PasswordCheckForm form = PasswordCheckForm.builder().password(UserFactory.userpassword).build();
        ObjectMapper objectMapper = new ObjectMapper();

        //when
        mockMvc.perform(post("/users/seller/transfer",seller.getUserId())
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk());

        //then
    }

    @Test
    @Transactional
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void transferSellerToUser_실패_invalid_auth() throws Exception {
        //given
        User seller = userFactory.createSeller("user");

        PasswordCheckForm form = PasswordCheckForm.builder().password(UserFactory.userpassword).build();
        ObjectMapper objectMapper = new ObjectMapper();

        //when
        mockMvc.perform(post("/users/seller/transfer",seller.getUserId())
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andDo(print());

        //then
    }

}
