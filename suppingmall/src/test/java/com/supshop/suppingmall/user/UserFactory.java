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
@ActiveProfiles("test")
@Transactional
public class UserFactory {

    @Autowired UserMapper userMapper;
    @Autowired UserService userService;
    @Autowired ModelMapper modelMapper;
    public static final String userpassword = "kkk111222333!";


    public User createAdmin(String name) {
        name = "admin";
        String email = name+"@email.com";

        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.setCreatedDate(null);
            return user;
        }

        User admin = buildUser(name);
        admin.setRole(Role.ADMIN);
        userMapper.insertUser(admin);

        return admin;
    }

    public User createSeller(String name) {

        User seller = buildUser("seller");

        StoreVO store = StoreVO.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("N")
                .build();

        seller.setStoreVO(store);

        userService.patchUser(seller.getUserId(), seller);
        return seller;
    }

    public User createApplicant(String name) {
        name = "applicant";
        String email = name+"@email.com";
        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.setCreatedDate(null);
            return user;
        }
        StoreVO store = StoreVO.builder()
                .storeName("섭프라이즈스토어")
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("N")
                .build();

        User user = buildUser(name);
        user.setStoreVO(store);
        userService.createUser(user);
        return user;
    }

    public User createUser(String name) {

        String email = name+"@email.com";
        Optional<User> userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.setCreatedDate(null);
            return user;
        }

        User user = buildUser(name);
        userService.createUser(user);
        return user;
    }

    public User buildUser(String name){
        String email = name+"@email.com";

        StoreVO storeVO = StoreVO.builder().storeApplyYn("N").build();
        return User.builder()
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
                .storeVO(storeVO)
                .build();
    }


    /**
     * UserDetails를 상속한 SessionUser를 생성하기 위한 userDetailsService
     * 테스트시, @WithSessionUser 사용시 해당 빈을 사용하여 유저를 생성함
     * @return user
     */
    @Bean(value = "userDetailsService")
    @Profile("test")
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User tester;
                if(username.equals("admin")) {
                    tester = createAdmin(username);
                } else if(username.equals("seller")) {
                    tester = createSeller(username);
                } else {
                    tester = createUser(username);
                }
                SessionUser map = modelMapper.map(tester, SessionUser.class);
                return map;
            }
        };
    }

}
