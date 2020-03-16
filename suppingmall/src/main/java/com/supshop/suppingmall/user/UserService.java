package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import com.supshop.suppingmall.page.Criteria;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public List<User> getAllUser(Criteria criteria, String type, String searchValue) {
        return userMapper.selectAllUser(criteria,type,searchValue);
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

    public User getUserByEmail(String email) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return user;
    }

    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    public void updateUser(Long id, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updateUser(id, user);
    }

    public void patchUser(Long id, User user) {
        userMapper.patchUser(id, user);
    }

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
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities(Arrays.asList(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> authorities(List<User.Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+ role.name()))
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
}
