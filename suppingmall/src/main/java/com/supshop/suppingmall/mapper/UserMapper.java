package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.user.User;

import java.util.List;

public interface UserMapper {

    List<User> selectAllUser();

    User selectUser(int id);

    User selectUserByEmail(String email);

    void insertUser(User user);

    void updateUser(int id, User user);

    void deleteUSer(int id);
}
