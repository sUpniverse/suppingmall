package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.user.StoreVO;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;

import java.util.List;
import java.util.Optional;

public interface UserMapper {

    List<User> selectAllUser(Criteria criteria, String type, String searchValue);

    User selectUser(Long id);

    Optional<User> selectUserByEmail(String email);

    void insertUser(User user);

    void updateUser(Long id, User user);

    void deleteUSer(Long id);

    int selectUserCount();

    void patchUser(Long id, User user);

    List<User> selectAllStore(Criteria criteria, String type, String searchValue);
}
