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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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
public class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserFactory userFactory;
    @Autowired ModelMapper modelMapper;
    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void getSignUpForm_성공_비로그인_유저() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signup"))
                .andDo(print());

        //then

    }

    @Test
    @WithMockUser()
    public void getSignUpForm_실패_로그인_유저() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/signup"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    @WithAnonymousUser
    public void getLoginForm_비로그인() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/loginform"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/login"))
                .andDo(print());


        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getLoginForm_로그인유저() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/loginform"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());


        //then

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getAllUser_성공_운영자() throws Exception {
        //given
        User admin = userFactory.createAdmin("admin");

        //when
        mockMvc.perform(get(("/users")))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/list"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getAllUser_실패_운영자_아닌_회원() throws Exception {
        //given
        User admin = userFactory.createUser("user");

        //when
        mockMvc.perform(get(("/users")))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    public void login_성공() throws Exception {
        //given
        User user = userFactory.buildUser("user");
        String password = user.getPassword();
        userService.createUser(user);
        //when
        mockMvc.perform(post("/users/login")
                .with(csrf())
                .param("username", user.getEmail())
                .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());

        //then

    }

    @Test
    public void login_실패_아이디_혹은_패스워드오류() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(post("/users/login")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/loginform?error"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUserById_성공() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/main"))
                .andDo(print())
        ;
        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUserById_실패_주인이아닌_요청() throws Exception {
        //given
        User user = userFactory.createUser("user");
        Long id = 0l;

        //when
        mockMvc.perform(get("/users/{id}",id))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print())
        ;
        //then

    }

    @Test
    public void createUser_Validation_성공() throws Exception {
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

                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    public void createUser_Validation_실패_잘못된_이메일형식() throws Exception {
        //given
        String username = "test@gmai";
        String userpassword = "k12345!";
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
                .with(csrf())
                .header("Referer", "/users/signup")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/signup?error"))
                .andDo(print());

        //then

    }

    @Test
    public void createUserValidation_실패_잘못된_패스워드() throws Exception {
        //given
        String username = "test@gmail.com";
        String userpassword = "k12345"; //특수 문자가 없는 경우
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
                .header("Referer", "/users/signup")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/signup?error"))
                .andDo(print());

        //then

    }

    @Test
    public void createUserValidation_실패_잘못된_전화번호() throws Exception {
        //given
        String username = "test@gmail.com";
        String userpassword = "kss12345!"; //특수 문자가 없는 경우
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
                .header("Referer", "/users/signup")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/signup?error"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdateForm_성공_로그인_유저() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/updateform",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/updateForm"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdateForm_성공_로그인_운영자() throws Exception {
        //given
        User admin = userFactory.createAdmin("admin");
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/updateform",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/adminUpdateForm"))
                .andDo(print());

        //then

    }

    @Test
    public void getUpdateForm_실패_비로그인_유저() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/{id}/updateform",1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdateForm_실패_로그인_다른유저() throws Exception {
        //given
        userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/updateform",1))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }



    @Test
    public void pathchUserWithOutDelYn() throws Exception {
        //given
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
        )
                .andExpect(status().isOk())
                .andDo(print());
        //then

    }

    @Test
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
