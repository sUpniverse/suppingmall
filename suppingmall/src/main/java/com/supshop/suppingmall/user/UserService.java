package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.TokenGenerator;
import com.supshop.suppingmall.error.exception.DuplicateException;
import com.supshop.suppingmall.event.EventType;
import com.supshop.suppingmall.mapper.UserMapper;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public List<User> getAllUser(Criteria criteria, String type, String searchValue) {
        return userMapper.findAll(criteria,type,searchValue);
    }

    /**
     * 판매자 지원한 유저의 목록을 반환
     * @param thirtyItemsCriteria
     * @return 판매자 지원자 목록
     */
    public List<User> getApplySellerUsers(ThirtyItemsCriteria thirtyItemsCriteria) {
        return userMapper.findApplySeller(thirtyItemsCriteria);
    }


    public int getUserCount(String type, String searchValue) {
        return userMapper.selectUserCount(type, searchValue);
    }

    public User getUser(Long id) {
        return userMapper.findOne(id);
    }

    /**
     * UserConfirmation의 정보를 포함하여 User정보 가져온다.
     * @param email
     * @return
     */
    public Optional<User> getUserWithConfirmationByEmail(String email) {
        return userMapper.findUserConfirmationById(email);
    }

    /**
     * 회원가입
     * 이미 존재하는 회원이 있는지 확인 후 (by Email),
     * 존재하지 않으면, 회원가입을 하고 확인요청 이메일을 발송한다.
     * 이메일 발송은 비동기로 진행된다.
     * @param user
     * @return createdUser
     */
    @Transactional
    public User createUser(User user) {

        if(isUserAlreadyExistByEmail(user.getEmail())) {
           throw new DuplicateException();
        }
        //passowrodEncoder를 통한 입력된 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailConfirmYn("N");

        int insert = userMapper.insertUser(user);

        // 회원정보 삽입에 상공하면 이메일 전송
        if(insert == 1) {
            sendConfirmationEmail(user);
        }

        return user;

    }

    /**
     * OAuth 유저 회원가입
     * 이미 존재하는 회원이 있는지 확인 후 (by Email)
     * 존재하면, 이미 가입했던 이력의 유저를 반환
     * 존재하지 않으면, 회원 가입을 한다.
     * 이미 OAuth를 통해 이메일에 대한 검증은 됐다고 생각하므로,
     * 따로 이메일 발송을 통한 검증은 진행하지 않는다.
     * @param user
     * @return createdUser
     */
    @Transactional
    public User createUserByOAuth(User user) {

        User existUser;
        try {
            existUser = getUserByEmail(user.getEmail());
        } catch (UsernameNotFoundException e){
            user.setEmailConfirmYn("Y");
            userMapper.insertUser(user);
            return user;
        }
        return existUser;
    }


    @Transactional
    public void updateUser(Long id, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updateUser(id, user);
    }

    @Transactional
    public void patchUser(Long id, User user) {
        userMapper.patchUser(id, user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userMapper.deleteUSer(id);
    }

    public List<SessionUser> getStore(String type, String value) {
        List<User> users = userMapper.selectAllStore(null, type, value);
        List<SessionUser> sessionUserList = new ArrayList<>();
        for(User user : users) {
            SessionUser map = modelMapper.map(user, SessionUser.class);
            sessionUserList.add(map);
        }
        return sessionUserList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        SessionUser sessionUser = modelMapper.map(user, SessionUser.class);
        return sessionUser;
    }


    //유저의 비밀번호 검증시
    protected boolean matchedPassword(String email, String password) {
        String encodedPassword = userMapper.findUserByEmail(email).get().getPassword();
        return passwordEncoder.matches(password,encodedPassword);
    }


    public void resendConfirmationEmail(User user) {
        eventPublisher.publishEvent(new UserEvent(EventType.CREATED, LocalDateTime.now(),user));
    }

    /**
     * 유저 회원 가입 후, 실제 존재하는 메일인지 확인하기 위해 이메일을 발송한다
     * @Async 어노테이션을 이용해 비동기적으로 처리 되며,
     * 토큰을 생성하여 eventPublisher로 메일 전송 이벤트를 publish 한다.
     * @param user
     */
    @Async
    void sendConfirmationEmail(User user) {
        String token = TokenGenerator.issueToken();
        UserConfirmation userConfirmation = UserConfirmation.builder()
                .userId(user.getUserId())
                .confirmToken(token)
                .build();
        int result = userMapper.saveConfirmation(userConfirmation);
        user.setUserConfirmation(userConfirmation);

        if(result == 1) {
            eventPublisher.publishEvent(new UserEvent(EventType.CREATED, LocalDateTime.now(),user));
        }
    }

    @Transactional
    @Async
    public void sendChangePasswordEmail(User user) {

        String token = TokenGenerator.issuePassword();

        user.setPassword(passwordEncoder.encode(token));

        int result = userMapper.updateUser(user.getUserId(), user);

        if(result == 1) {
            user.setPassword(token);
            eventPublisher.publishEvent(new UserEvent(EventType.UPDATED, LocalDateTime.now(),user));
        }
    }


    protected User getUserByEmail(String email) {
        User user = userMapper.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return user;
    }

    public boolean isUserAlreadyExistByEmail(String email) {
        try {
            getUserByEmail(email);
        } catch (UsernameNotFoundException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
