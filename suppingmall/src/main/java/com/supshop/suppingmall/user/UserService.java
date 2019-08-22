package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUser(String id) {
        return userMapper.selectUser(id);
    }

    public String createUser(User user) {
        return userMapper.insertUser(user);
    }

    public String updateUser(String id, User user) {
        return userMapper.updateUser(id, user);
    }

    public String deleteUser(String id) {
        return userMapper.deleteUSer(id);
    }
}
