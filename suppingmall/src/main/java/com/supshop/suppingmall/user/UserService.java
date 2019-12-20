package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAllUser() {
        return userMapper.selectAllUser();
    }

    public User getUser(Long id) {
        return userMapper.selectUser(id);
    }

    public User getUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    public void createUser(User user) {
        userMapper.insertUser(user);
    }

    public void updateUser(Long id, User user) {
        userMapper.updateUser(id, user);
    }

    public void deleteUser(Long id) {
        userMapper.deleteUSer(id);
    }
}
