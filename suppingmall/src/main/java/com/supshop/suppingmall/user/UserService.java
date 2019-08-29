package com.supshop.suppingmall.user;

import com.supshop.suppingmall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> getAllUser() {
        return userMapper.selectAllUser();
    }

    public User getUser(int id) {
        return userMapper.selectUser(id);
    }

    public User getUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    public void createUser(User user) {
        userMapper.insertUser(user);
    }

    public void updateUser(int id, User user) {
        userMapper.updateUser(id, user);
    }

    public void deleteUser(int id) {
        userMapper.deleteUSer(id);
    }
}
