package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.user.User;

import java.util.List;

public interface UserMapper {

    List<User> selectAllUser();

    User selectUser(Long id);

    User selectUserByEmail(String email);

    void insertUser(User user);

    void updateUser(Long id, User user);

    void deleteUSer(Long id);
}
