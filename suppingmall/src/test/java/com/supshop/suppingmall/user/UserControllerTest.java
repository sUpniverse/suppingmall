package com.supshop.suppingmall.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.user.Form.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired ObjectMapper objectMapper;

    @Test
    public void getSignUpForm_성공() throws Exception {
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
    public void getSignUpForm_실패_already_login() throws Exception {
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
    public void getLoginForm_성공() throws Exception {
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
    public void getLoginForm_실패_already_login() throws Exception {
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
    public void getAllUser_성공_admin() throws Exception {
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
    public void getAllUser_실패_no_authUser() throws Exception {
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
        String userpassword = UserFactory.userpassword;
        SignUpForm form = SignUpForm.builder()
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
                                    .param("name",form.getName())
                                    .param("nickName",form.getNickName())
                                    .param("address",form.getAddress())
                                    .param("addressDetail",form.getAddressDetail())
                                    .param("zipCode",form.getZipCode())
                                    .param("phoneNumber",form.getPhoneNumber())
                                    .param("role",form.getRole().toString())
                                    .param("type",form.getType().toString())

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
        String userpassword = UserFactory.userpassword;
        SignUpForm form = SignUpForm.builder()
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
                .param("name",form.getName())
                .param("nickName",form.getNickName())
                .param("address",form.getAddress())
                .param("addressDetail",form.getAddressDetail())
                .param("zipCode",form.getZipCode())
                .param("phoneNumber",form.getPhoneNumber())
                .param("role",form.getRole().toString())
                .param("type",form.getType().toString())
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
        SignUpForm form = SignUpForm.builder()
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
                .param("name",form.getName())
                .param("nickName",form.getNickName())
                .param("address",form.getAddress())
                .param("addressDetail",form.getAddressDetail())
                .param("zipCode",form.getZipCode())
                .param("phoneNumber",form.getPhoneNumber())
                .param("role",form.getRole().toString())
                .param("type",form.getType().toString())
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
        mockMvc.perform(get("/users/{id}/form",user.getUserId()))
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
        mockMvc.perform(get("/users/{id}/form",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/adminUpdateForm"))
                .andDo(print());

        //then

    }

    @Test
    public void getUpdateForm_실패_비로그인_유저() throws Exception {
        //given


        //when
        mockMvc.perform(get("/users/{id}/form",1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/loginform"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdateForm_실패_로그인_다른유저() throws Exception {
        //given
        userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/form",1))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }

    @Test
    @WithSessionUser(value = "user")
    public void update_성공() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        UpdateUserForm form = modelMapper.map(user, UpdateUserForm.class);

        form.setPassword(UserFactory.userpassword);
        form.setNickName("바바바보보보");
        form.setAddress("제주시");
        form.setAddressDetail("애월읍");
        form.setPhoneNumber("010-3333-5555");

        mockMvc.perform(put("/users/{id}",user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }

    @Test
    @WithSessionUser(value = "user")
    public void update_실패_다른유저_요청() throws Exception {
        //given
        User user = userFactory.createUser("user");
        User user2 = userFactory.createUser("user2");

        //when
        UpdateUserForm form = modelMapper.map(user, UpdateUserForm.class);

        form.setPassword(UserFactory.userpassword);
        form.setNickName("바바바보보보");
        form.setAddress("제주시");
        form.setAddressDetail("애월읍");
        form.setPhoneNumber("010-3333-5555");

        mockMvc.perform(put("/users/{id}",user2.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then

    }

    @Test
    @WithSessionUser(value = "user")
    public void update_실패_틀린_패스워드() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        UpdateUserForm form = modelMapper.map(user, UpdateUserForm.class);

        form.setPassword(UserFactory.userpassword+"1");
        form.setNickName("바바바보보보");
        form.setAddress("제주시");
        form.setAddressDetail("애월읍");
        form.setPhoneNumber("010-3333-5555");

        mockMvc.perform(put("/users/{id}",user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then

    }


    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdatePasswordForm_성공() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/password",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/passwordForm"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getUpdatePasswordForm_실패_다른유저_요청() throws Exception {
        //given
        userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/password",1))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void updatePassword_성공() throws Exception {
        //given
        User tester = userFactory.createUser("user");
        UpdatePasswordForm form = UpdatePasswordForm.builder()
                .password(UserFactory.userpassword)
                .newPassword(UserFactory.userpassword+1)
                .newPasswordCheck(UserFactory.userpassword+1)
                .build();


        //when

        mockMvc.perform(put("/users/{id}/password",tester.getUserId())
                .content(objectMapper.writeValueAsString(form))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void updatePassword_실패_잘못된_패스워드() throws Exception {
        //given
        User tester = userFactory.createUser("user");
        UpdatePasswordForm form = UpdatePasswordForm.builder()
                .password(UserFactory.userpassword+1)
                .newPassword(UserFactory.userpassword+1)
                .newPasswordCheck(UserFactory.userpassword+1)
                .build();


        //when

        mockMvc.perform(put("/users/{id}/password",tester.getUserId())
                .content(objectMapper.writeValueAsString(form))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void updatePassword_실패_invalid_newPassword() throws Exception {
        //givenBB
        User tester = userFactory.createUser("user");
        UpdatePasswordForm form = UpdatePasswordForm.builder()
                .password(UserFactory.userpassword)
                .newPassword(UserFactory.userpassword+1111)
                .newPasswordCheck(UserFactory.userpassword)
                .build();


        //when

        mockMvc.perform(put("/users/{id}/password",tester.getUserId())
                .content(objectMapper.writeValueAsString(form))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void updatePassword_실패_different_newPasswordCheck() throws Exception {
        //given
        User tester = userFactory.createUser("user");
        UpdatePasswordForm form = UpdatePasswordForm.builder()
                .password(UserFactory.userpassword)
                .newPassword(UserFactory.userpassword+1)
                .newPasswordCheck(UserFactory.userpassword)
                .build();


        //when

        mockMvc.perform(put("/users/{id}/password",tester.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf())
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getSignOutForm_성공() throws Exception {
        //given
        User user = userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/signout",user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signout"))
                .andDo(print());

        //then

    }
    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void getSignOutForm_실패_다른유저_요청() throws Exception {
        //given
        userFactory.createUser("user");

        //when
        mockMvc.perform(get("/users/{id}/signout",1))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void signOut() throws Exception {
        //given
        User tester = userFactory.createUser("tester");
        PasswordCheckForm form = PasswordCheckForm.builder().password(UserFactory.userpassword).build();


        //when

        mockMvc.perform(delete("/users/{id}",tester.getUserId())
                .content(objectMapper.writeValueAsString(form))
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
    public void signOut_실패_잘못된_패스워드() throws Exception {
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


    @Test
    public void getFindPasswordForm() throws Exception {
        //given


        //when

        mockMvc.perform(get("/users/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/findAccountForm"))
                .andDo(print());

        //then

    }

    @Test
    public void getFindPassword() throws Exception {
        //given
        User user = userFactory.createUser("user");

        FindAccountForm form = FindAccountForm.builder()
                .email(user.getEmail())
                .userName(user.getName())
                .build();

        //when

        mockMvc.perform(post("/users/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }

    @Test
    public void getFindPassword_실패_존재하지않는_회원() throws Exception {
        //given
        User user = userFactory.createUser("user");

        FindAccountForm form = FindAccountForm.builder()
                .email(user.getEmail())
                .userName(user.getName())
                .build();

        //when

        mockMvc.perform(post("/users/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void confirmUser() throws Exception {
        //given
        User tester = userFactory.createUser("tester");
        User user = userService.getUserWithConfirmationByEmail(tester.getEmail()).get();
        String token = user.getUserConfirmation().getConfirmToken();

        //when

        mockMvc.perform(get("/users/confirm")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/confirm/success"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void confirmUser_실패_잘못된_토큰() throws Exception {
        //given
        User tester = userFactory.createUser("tester");

        //when

        mockMvc.perform(get("/users/confirm")
                .param("token", "aaas22313"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void confirmUser_실패_already_authenticated_user() throws Exception {
        //given
        User user = userFactory.createUser("user");
        user.setEmailConfirmYn("Y");
        userService.patchUser(user.getUserId(), user);

        //when

        mockMvc.perform(get("/users/confirm")
                .param("token", "aaas22313"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error/400"))
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void resendConfirmEmail() throws Exception {
        //given
        User tester = userFactory.createUser("tester");

        //when

        mockMvc.perform(post("/users/confirm/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }

    @Test
    @WithUserDetails(value = "tester", userDetailsServiceBeanName = "userDetailsService")
    public void resendConfirmEmail_실패_존재하지않는_유저() throws Exception {
        //given
        User tester = userFactory.createUser("tester");

        //when

        mockMvc.perform(post("/users/confirm/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }
}
