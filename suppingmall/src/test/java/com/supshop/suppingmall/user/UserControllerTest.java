package com.supshop.suppingmall.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserFactory userFactory;
    @Autowired ModelMapper modelMapper;
    @Autowired UserService userService;
    private MockHttpSession session;

    @Test
    public void getUserById() throws Exception {
        //given
        Long id = 1l;

        //when
        mockMvc.perform(get("/users/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(id))
                .andDo(print())
        ;
        //then

    }

    @Test
    public void getUserByEmail() throws Exception {
        //given
        String email = "kms22345@naver.com";

        //when
        mockMvc.perform(get("/users/emails/{email}",email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(print())
        ;
        //then
    }

    @Test
    public void getUserByEmailNotFound() throws Exception {
        //given
        String email = "kms22345@google.com";

        //when
        mockMvc.perform(get("/users/emails/{email}",email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print())
        ;
        //then
    }

    @Test
    @Transactional
    public void UserValidation() throws Exception {
        //given
        String username = "test@gmail.com";
        String userpassword = "kms9109111!";
        User user = User.builder()
                .email(username)
                .password(userpassword)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("01033334444")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        mockMvc.perform(post("/users")
                                    .param("email",username)
                                    .param("password",userpassword)
                                    .param("name",user.getName())
                                    .param("nickName",user.getNickName())
                                    .param("address",user.getAddress())
                                    .param("addressDetail",user.getAddressDetail())
                                    .param("zipCode",user.getZipCode())
                                    .param("phoneNumber",user.getPhoneNumber())
                                    .param("role",user.getRole().toString())
                                    .param("type",user.getType().toString())
                                    )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    @Transactional
    public void UserValidationWrongEmail() throws Exception {
        //given
        String username = "test@gmai";
        String userpassword = "kms9109111!";
        User user = User.builder()
                .email(username)
                .password(userpassword)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("01033334444")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        mockMvc.perform(post("/users")
                .param("email",username)
                .param("password",userpassword)
                .param("name",user.getName())
                .param("nickName",user.getNickName())
                .param("address",user.getAddress())
                .param("addressDetail",user.getAddressDetail())
                .param("zipCode",user.getZipCode())
                .param("phoneNumber",user.getPhoneNumber())
                .param("role",user.getRole().toString())
                .param("type",user.getType().toString())
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then

    }

    @Test
    @Transactional
    public void UserValidationWrongPassword() throws Exception {
        //given
        String username = "test@gmail.com";
        String userpassword = "kms910911"; //특수 문자가 없는 경우
        User user = User.builder()
                .email(username)
                .password(userpassword)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("01033334444")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        mockMvc.perform(post("/users")
                .param("email",username)
                .param("password",userpassword)
                .param("name",user.getName())
                .param("nickName",user.getNickName())
                .param("address",user.getAddress())
                .param("addressDetail",user.getAddressDetail())
                .param("zipCode",user.getZipCode())
                .param("phoneNumber",user.getPhoneNumber())
                .param("role",user.getRole().toString())
                .param("type",user.getType().toString())
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then

    }

    @Test
    @Transactional
    public void UserValidationWrongPhone() throws Exception {
        //given
        String username = "test@gmail.com";
        String userpassword = "kms910911"; //특수 문자가 없는 경우
        User user = User.builder()
                .email(username)
                .password(userpassword)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("0102223333")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        mockMvc.perform(post("/users")
                .param("email",username)
                .param("password",userpassword)
                .param("name",user.getName())
                .param("nickName",user.getNickName())
                .param("address",user.getAddress())
                .param("addressDetail",user.getAddressDetail())
                .param("zipCode",user.getZipCode())
                .param("phoneNumber",user.getPhoneNumber())
                .param("role",user.getRole().toString())
                .param("type",user.getType().toString())
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then

    }

    private void addUserInSession(User user) {
        SessionUser needUser = modelMapper.map(user, SessionUser.class);
        session = new MockHttpSession();
        session.setAttribute("user",needUser);

    }


    @Test
    @Transactional
    public void pathchUserWithOutDelYn() throws Exception {
        //given
        addUserInSession(userFactory.createUser("test"));
        ObjectMapper objectMapper = new ObjectMapper();
        Long userId = 1l;
        String username = "kmsup2@gmail.com";
        SessionUser user = SessionUser.builder()
                .email(username)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("0102223333")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        mockMvc.perform(patch("/users/{id}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .session(session)
        )
                .andExpect(status().isOk())
                .andDo(print());
        //then

    }

    @Test
    @Transactional
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void signOut() throws Exception {
        //given
        User tester = userFactory.createUser("tester");
        String password = "{\"password\":\"sup2\"}";


        //when

        mockMvc.perform(delete("/users/{id}",tester.getUserId())
                    .content(password)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
        )
                    .andExpect(status().isOk())
                    .andDo(print());
        User user = userService.getUser(tester.getUserId());
        //then
        Assert.assertEquals("Y",user.getDelYn());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void signOutWithWrongPassword() throws Exception {
        //given
        User tester = userFactory.createUser("tester");
        String password = "{\"password\":\"22345335\"}";


        //when

        mockMvc.perform(delete("/users/{id}",tester.getUserId())
                .content(password)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andDo(print());
        //then
    }


}
