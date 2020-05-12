package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    @Autowired UserMapper userMapper;

    public User createAdmin(String name) {

        String username = name+"@email.com";
        String userpassword = "sup2";
        User user = User.builder()
                .email(username)
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
        userMapper.insertUser(user);
        return user;
    }

    public User createSeller(String name) {

        String username = name+"@email.com";
        String userpassword = "sup2";
        User user = User.builder()
                .email(username)
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
        userMapper.insertUser(user);
        return user;
    }

    public User createUser(String name) {

        String username = name+"@email.com";
        String userpassword = "sup2";
        User user = User.builder()
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
        userMapper.insertUser(user);
        return user;
    }
}
