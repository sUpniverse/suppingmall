package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.TokenGenerator;
import com.supshop.suppingmall.mapper.UserMapper;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public List<User> getAllUser(BoardCriteria boardCriteria, String type, String searchValue) {
        return userMapper.selectAllUser(boardCriteria,type,searchValue);
    }

    public List<User> getApplySellerUsers(BoardCriteria boardCriteria) {
        return userMapper.findUserByApplySellerYn(boardCriteria);
    }


    public int getBoardCount() {
        return userMapper.selectUserCount();
    }

    public User getUser(Long id) {
        return userMapper.selectUser(id);
    }

    //UserConfirmation의 정보를 같이 가져옴
    public Optional<User> getUserWithConfirmationByEmail(String email) {
        return userMapper.findUserConfirmationById(email);
    }

    @Transactional
    public User createUser(User user) throws RuntimeException {
        User existUser = getUserByEmail(user.getEmail());
        if(existUser != null) {
            return existUser;
        }

        if(user.getType().equals(User.LoginType.LOCAL)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userMapper.insertUser(user);
        sendConfirmationEmail(user);

        return user;
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

    public void resendConfirmationEmail(User user) {
        eventPublisher.publishEvent(new UserCreatedEvent(user, LocalDateTime.now()));
    }


    private void sendConfirmationEmail(User user) {
        String token = TokenGenerator.issueToken();
        UserConfirmation userConfirmation = UserConfirmation.builder()
                .userId(user.getUserId())
                .confirmToken(token)
                .build();
        int result = userMapper.saveConfirmation(userConfirmation);
        user.setUserConfirmation(userConfirmation);

        if(result == 1) {
            eventPublisher.publishEvent(new UserCreatedEvent(user, LocalDateTime.now()));
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
