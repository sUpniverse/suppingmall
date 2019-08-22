package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.user.User;

public interface UserMapper {
    
    User selectUser(String id);

    String insertUser(User user);

    String updateUser(String id, User user);

    String deleteUSer(String id);
}
