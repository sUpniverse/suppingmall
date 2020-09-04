package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ActiveProfiles("test")
public class UserFactory {

    @Autowired UserMapper userMapper;
    @Autowired UserService userService;
    @Autowired ModelMapper modelMapper;

    @Transactional
    public SessionUser createAdminToSession(String name) {

        String email = name+"@email.com";
        String userpassword = "sup2";
        User user = User.builder()
                .email(email)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();
        userService.createUser(user);
        SessionUser sessionUser = modelMapper.map(user, SessionUser.class);
        return sessionUser;
    }

    @Transactional
    public User createAdmin(String name) {

        String email = name+"@email.com";

        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            return userByEmail.get();
        }
        String userpassword = "sup2";
        User user = User.builder()
                .email(email)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .role(Role.getCodeString(Role.ADMIN.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();

        return user;
    }

    @Transactional
    public User createSeller(String name) {

        String email = name+"@email.com";
        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            return userByEmail.get();
        }
        String userpassword = "sup2";
        User user = User.builder()
                .email(email)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .role(Role.getCodeString(Role.SELLER.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();
        userService.createUser(user);
        return user;
    }

    @Transactional
    public User createApplicant(String name) {

        String email = name+"@email.com";
        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            return userByEmail.get();
        }
        String userpassword = "sup2";
        StoreVO store = StoreVO.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("Y")
                .build();

        User user = User.builder()
                .email(email)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .storeVO(store)
                .role(Role.getCodeString(Role.SELLER.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();
        userService.createUser(user);
        return user;
    }

    @Transactional
    public User createUser(String name) {

        String email = name+"@email.com";
        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            return userByEmail.get();
        }
        String userpassword = "sup2";
        User user = User.builder()
                .email(email)
                .password(userpassword)
                .name(name)
                .nickName(name)
                .address("운영자의 집")
                .addressDetail("그건 엄마집")
                .zipCode("00000")
                .phoneNumber("010-0000-0000")
                .delYn("N")
                .emailConfirmYn("N")
                .role(Role.getCodeString(Role.USER.getCode()))
                .type(User.LoginType.getCodeString(User.LoginType.LOCAL.getCode()))
                .build();
        userService.createUser(user);
        return user;
    }


        @Bean(value = "userDetailsService")
        @Profile("test")
        public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User tester = createUser(username);
                SessionUser map = modelMapper.map(tester, SessionUser.class);
                return map;
            }
        };
    }

}
