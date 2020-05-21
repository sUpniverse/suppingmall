package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserConfirmation;

import java.util.List;
import java.util.Optional;

public interface UserMapper {

    List<User> selectAllUser(BoardCriteria boardCriteria, String type, String searchValue);

    User selectUser(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findUserByApplySellerYn(BoardCriteria boardCriteria);

    void insertUser(User user);

    void updateUser(Long id, User user);

    void deleteUSer(Long id);

    int selectUserCount();

    void patchUser(Long id, User user);

    List<User> selectAllStore(BoardCriteria boardCriteria, String type, String searchValue);

    int saveConfirmation(UserConfirmation userConfirmation);

    Optional<User> findUserConfirmationById(String email);
}
