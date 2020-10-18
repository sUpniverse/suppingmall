package com.supshop.suppingmall.common;

import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.SessionUser;

import javax.servlet.http.HttpSession;

public class UserUtils {


    //세션의 유저와 해당 목표물의 주인의 아이디가 같은지 확인
    public static boolean isOwner(Long id, SessionUser sessionUser) {
        return sessionUser != null && sessionUser.getUserId().equals(id);
    }

    public static boolean isAdmin(SessionUser sessionUser) {
        return sessionUser != null && (sessionUser.getRole().equals(Role.ADMIN) || (sessionUser.getRole().equals(Role.MASTER)));
    }

    public static boolean isSeller(SessionUser sessionUser) {
        return sessionUser != null && sessionUser.getRole().equals(Role.SELLER);
    }

    public static boolean isUser(SessionUser sessionUser) {
        return sessionUser != null && sessionUser.getRole().equals(Role.USER);
    }

}
