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

    private final String rolePrefix = "ROLE_";

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

    public UserVO getUserVO(Long id) {
        User user = userMapper.selectUser(id);
        UserVO vo = modelMapper.map(user, UserVO.class);
        return vo;
    }

    public Optional<User> getUserWithConfirmationByEmail(String email) {
        return userMapper.findUserConfirmationById(email);
    }

    @Transactional
    public User createUser(User user) throws RuntimeException {
        if(isUserAlreadyExistByEmail(user.getEmail())) {
            new RuntimeException();
        }

        if(user.getType().equals(User.LoginType.LOCAL)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userMapper.insertUser(user);
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

    public List<UserVO> getStore(String type, String value) {
        List<User> users = userMapper.selectAllStore(null, type, value);
        List<UserVO> userVOList= new ArrayList<>();
        for(User user : users) {
            UserVO map = modelMapper.map(user, UserVO.class);
            userVOList.add(map);
        }
        return userVOList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities(Arrays.asList(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(rolePrefix+ role.name()))
                .collect(Collectors.toSet());
    }

    public UserVO isSignedInUser(String email, String password) {
        User user = null;
        try {
            user = getUserByEmail(email);
        } catch (UsernameNotFoundException e) {
            return null;
        }
        if(passwordEncoder.matches(password,user.getPassword())) {
            UserVO userVO = modelMapper.map(user, UserVO.class);
            return userVO;
        }
        return null;
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
