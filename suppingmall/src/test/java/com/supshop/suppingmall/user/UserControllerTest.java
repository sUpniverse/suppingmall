package com.supshop.suppingmall.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

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
        String username = "kmsup2@gmail.com";
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
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
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
        String username = "kmsup2@gmai";
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
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
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
        String username = "kmsup2@gmail.com";
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
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
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
        String username = "kmsup2@gmail.com";
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
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
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
        session = new MockHttpSession();
        session.setAttribute("user",user);

    }

    private User makeMasterUser() {
        return User.builder().role(User.Role.MASTER).build();
    }

    @Test
    @Transactional
    public void pathchUserWithOutDelYn() throws Exception {
        //given
        addUserInSession(makeMasterUser());
        ObjectMapper objectMapper = new ObjectMapper();
        Long userId = 1l;
        String username = "kmsup2@gmail.com";
        UserVO user = UserVO.builder()
                .email(username)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("0102223333")
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
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
    public void patchUserWithDelYn() throws Exception {
        //given
        addUserInSession(makeMasterUser());
        ObjectMapper objectMapper = new ObjectMapper();
        Long userId = 1l;

        String delYn = "{\"delYn\":\"Y\"}";

        //when

        mockMvc.perform(delete("/users/{id}",userId)
                    .content(delYn)
                    .contentType(MediaType.APPLICATION_JSON)
                    .session(session)
        )
                    .andExpect(status().isOk())
                    .andDo(print());


        //then

    }


}
