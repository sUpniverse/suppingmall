package com.supshop.suppingmall.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Transactional
    public void findByUsername() throws Exception {
        //given
        String username = "kmsup2@gmail.com";
        String userpassword = "sup2";
        User user = User.builder()
                .email(username)
                .password(userpassword)
                .name("운영자")
                .nickName("운영자")
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .role(User.Role.getCodeString(User.Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        userService.createUser(user);

        //when
        UserDetailsService userDetailsService = (UserDetailsService) userService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        //then
        assertThat(userDetails.getPassword()).isEqualTo(userpassword);
    }

    @Test
    public void findByUsernameFail() throws Exception {
        //given
        String email = "random@gmail.com";
        
        //when
        try {
            userService.loadUserByUsername(email);
            fail("supposed to be failed");
        }catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(email);
        }
        
        //then
    }

    @Test
    @Transactional
    public void partialUpdateUser() throws Exception {
        //given
        Long userId = 1l;
        User updatedUser = User.builder()
                .delYn("Y")
                .nickName("스타벅스")
                .address(null)
                .zipCode(null)
                .phoneNumber(null)
                .build();
        //when
        userService.patchUser(userId,updatedUser);

        //then
        User user = userService.getUser(userId);
        assertThat(user.getDelYn()).isEqualTo(updatedUser.getDelYn());
        assertThat(user.getNickName()).isEqualTo(updatedUser.getNickName());
        assertThat(user.getAddress()).isNotEqualTo(updatedUser.getAddress());
        assertThat(user.getZipCode()).isNotEqualTo(updatedUser.getZipCode());
        assertThat(user.getPhoneNumber()).isNotEqualTo(updatedUser.getPhoneNumber());

    }
}
