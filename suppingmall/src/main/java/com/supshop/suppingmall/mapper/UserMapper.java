package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserConfirmation;

import java.util.List;
import java.util.Optional;

public interface UserMapper {

    int selectUserCount(String type, String searchValue);

    List<User> findAll(Criteria criteria, String type, String searchValue);

    User findOne(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findApplySeller(Criteria criteria);

    List<User> selectAllStore(Criteria criteria, String type, String searchValue);

    Optional<User> findUserConfirmationById(String email);

    int insertUser(User user);

    int updateUser(Long id, User user);

    void deleteUSer(Long id);

    void patchUser(Long id, User user);

    int saveConfirmation(UserConfirmation userConfirmation);

}
