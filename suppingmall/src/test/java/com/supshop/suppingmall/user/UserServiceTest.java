package com.supshop.suppingmall.user;

import com.supshop.suppingmall.error.exception.DuplicateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserFactory userFactory;

    @Test
    public void findUserByEmail_성공() throws Exception {
        //given
        User james = userFactory.createUser("james");

        //when
        SessionUser user = (SessionUser) userService.loadUserByUsername(james.getEmail());

        //then
        assertThat(user.getEmail()).isEqualTo(james.getEmail());
        assertThat(user.getName()).isEqualTo(james.getName());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findUserByEmail_실패() throws Exception {
        //given
        String email = "random@gmail.com";
        
        //when
        userService.loadUserByUsername(email);
    }

    @Test
    public void createUser_성공() throws Exception {
        //given
        int size = userService.getAllUser(null, null, null).size();
        User user = userFactory.buildUser("user");

        //when
        userService.createUser(user);
        int addedSize = userService.getAllUser(null, null, null).size();

        //then
        assertThat(addedSize).isEqualTo(size+1);

    }

    @Test(expected = DuplicateException.class)
    public void createUser_실패_이미존재유저() throws Exception {
        //given
        User user = userFactory.createUser("user");
        int size = userService.getAllUser(null, null, null).size();
        User duplicatedUser = userFactory.buildUser("user");

        //when
        userService.createUser(user);
        int addedSize = userService.getAllUser(null, null, null).size();

        //then
        assertThat(addedSize).isEqualTo(size);
    }

    @Test(expected = DataAccessException.class)
    public void createUser_실패_필수정보_미기입() throws Exception {
        //given
        int size = userService.getAllUser(null, null, null).size();
        User user = userFactory.buildUser("user");
        user.setRole(null);
        user.setType(null);

        //when
        userService.createUser(user);
        int addedSize = userService.getAllUser(null, null, null).size();

        //then
        assertThat(addedSize).isEqualTo(size);
    }

    @Test
    public void updateUser_성공() throws Exception{
        //given
        User user = userFactory.createUser("user");
        user.setNickName("admin");
        user.setRole(Role.ADMIN);
        user.setAddress(null);
        user.setZipCode(null);
        user.setPhoneNumber(null);

        //when
        userService.updateUser(user.getUserId(), user);
        User updatedUser = userService.getUser(user.getUserId());

        //then
        assertThat(updatedUser.getNickName()).isEqualTo(user.getNickName());
        assertThat(updatedUser.getRole()).isEqualTo(user.getRole());
        assertThat(updatedUser.getZipCode()).isNull();
        assertThat(updatedUser.getAddress()).isNull();
        assertThat(updatedUser.getPhoneNumber()).isNull();

    }

    @Test(expected = DataAccessException.class)
    public void updateUser_실패_필수정보_미기입() throws Exception{
        //given
        User user = userFactory.createUser("user");
        user.setNickName("admin");
        user.setRole(null);
        user.setType(null);
        user.setAddress(null);
        user.setZipCode(null);
        user.setPhoneNumber(null);

        //when
        userService.updateUser(user.getUserId(), user);
        User updatedUser = userService.getUser(user.getUserId());

        //then
        assertThat(updatedUser.getNickName()).isNotEqualTo(user.getNickName());
        assertThat(updatedUser.getRole()).isNotEqualTo(user.getRole());
        assertThat(updatedUser.getType()).isNotNull();
        assertThat(updatedUser.getZipCode()).isNotNull();
        assertThat(updatedUser.getAddress()).isNotNull();
        assertThat(updatedUser.getPhoneNumber()).isNotNull();

    }

    /**
     * JPA의 더티체킹과 같이 수정된 사항만 받아와 데이터베이스에 반영하도록
     * null이 아닌 데이터만 바뀌도록 수정
     */
    @Test
    public void partialUpdateUser() throws Exception {
        //given
        User user = userFactory.createUser("user");

        user.setNickName("스타벅스");
        user.setAddress(null);
        user.setZipCode(null);
        user.setPhoneNumber(null);
        //when
        userService.patchUser(user.getUserId(),user);

        //then
        User updatedUser = userService.getUser(user.getUserId());
        assertThat(updatedUser.getDelYn()).isEqualTo(user.getDelYn());
        assertThat(updatedUser.getNickName()).isEqualTo(user.getNickName());
        assertThat(updatedUser.getAddress()).isNotEqualTo(user.getAddress());
        assertThat(updatedUser.getZipCode()).isNotEqualTo(user.getZipCode());
        assertThat(updatedUser.getPhoneNumber()).isNotEqualTo(user.getPhoneNumber());

    }

    @Test
    public void applySeller() throws Exception {
        //given
        User user = userFactory.createUser("user");
        StoreVO store = StoreVO.builder()
                .storePrivateNumber("000-000-000")
                .storeAddress("서울시 중구 신당동 432")
                .storeAddressDetail("서프라이즈빌딩 502호")
                .storeZipCode("347532")
                .storeApplyYn("Y")
                .build();

        user.setStoreVO(store);

        //when
        userService.patchUser(user.getUserId(),user);

        //then
        User applyer = userService.getUser(user.getUserId());
        assertThat(applyer.getStoreVO().getStorePrivateNumber()).isEqualTo(store.getStorePrivateNumber());
        assertThat(applyer.getStoreVO().getStoreName()).isEqualTo(store.getStoreName());
        assertThat(applyer.getStoreVO().getStoreZipCode()).isEqualTo(store.getStoreZipCode());
        assertThat(applyer.getStoreVO().getStoreAddress()).isEqualTo(store.getStoreAddress());
        assertThat(applyer.getStoreVO().getStoreAddressDetail()).isEqualTo(store.getStoreAddressDetail());
        assertThat(applyer.getStoreVO().getStorePhoneNumber()).isEqualTo(store.getStorePhoneNumber());
        assertThat(applyer.getStoreVO().getStoreApplyYn()).isEqualTo(store.getStoreApplyYn());
    }

    /**
     * 유저 생성시 실제 존재하는 이메일의 회원인지를
     * 판단하기 위하여 판단 여부를 생성
     */
    @Test
    public void createUserConfirmation_성공_byLocalType() throws Exception {
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
        assertThat(user.getEmailConfirmYn()).isEqualTo("N");
        assertThat(user.getUserConfirmation().getConfirmToken()).isNotEmpty();
    }

    /**
     * Oauth를 이용해 유저 생성시에는
     * 이메일 검증이 필요없음
     */
    @Test
    public void createUserConfirmation_성공_byOAuth() throws Exception {
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
                .type(User.LoginType.getCodeString(User.LoginType.KAKAO.getCode()))
                .build();

        //when
        userService.createUserByOAuth(testUser);
        User user = userService.getUserWithConfirmationByEmail(testUser.getEmail()).get();


        //then
        assertThat(user.getEmailConfirmYn()).isEqualTo("Y");
        assertThat(user.getUserConfirmation()).isNull();
    }

}
