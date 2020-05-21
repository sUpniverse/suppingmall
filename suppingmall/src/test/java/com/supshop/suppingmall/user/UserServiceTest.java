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

    @Autowired UserService userService;
    @Autowired UserFactory userFactory;

    @Test
    @Transactional
    public void findByUsername() throws Exception {
        //given
        User james = userFactory.createAdmin("james");

        //when
        UserDetailsService userDetailsService = (UserDetailsService) userService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(james.getEmail());

        //then
        assertThat(userDetails.getPassword()).isEqualTo(james.getPassword());
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

    @Test
    @Transactional
    public void applySeller() throws Exception {
        //given
        Long userId = 1l;
        StoreVO store = StoreVO.builder()
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("Y")
                .build();
        User userApply = User.builder().storeVO(store).build();

        //when
        userService.patchUser(userId,userApply);

        //then
        User user = userService.getUser(userId);
        assertThat(user.getStoreVO().getStorePrivateNumber()).isEqualTo(store.getStorePrivateNumber());
        assertThat(user.getStoreVO().getStoreName()).isEqualTo(store.getStoreName());
        assertThat(user.getStoreVO().getStoreZipCode()).isEqualTo(store.getStoreZipCode());
        assertThat(user.getStoreVO().getStoreAddress()).isEqualTo(store.getStoreAddress());
        assertThat(user.getStoreVO().getStoreAddressDetail()).isEqualTo(store.getStoreAddressDetail());
        assertThat(user.getStoreVO().getStorePhoneNumber()).isEqualTo(store.getStorePhoneNumber());
        assertThat(user.getStoreVO().getStoreApplyYn()).isEqualTo(store.getStoreApplyYn());
    }
    
    @Test
    @Transactional
    public void createUserConfirmation() throws Exception {
        //given
        String name = "test";
        String username = name+"@email.com";
        String userpassword = "sup2";
        User testUser = User.builder()
                .email(username)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .role(Role.getCodeString(Role.USER.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        //when
        userService.createUser(testUser);
        User user = userService.getUserWithConfirmationByEmail(testUser.getEmail()).get();


        //then
        UserConfirmation testUserConfirmation = testUser.getUserConfirmation();
        UserConfirmation userConfirmation = user.getUserConfirmation();

        assertThat(testUser.getEmailConfirmYn()).isEqualTo(user.getEmailConfirmYn());
        assertThat(testUserConfirmation.getConfirmToken()).isEqualTo(userConfirmation.getConfirmToken());
    }
}
