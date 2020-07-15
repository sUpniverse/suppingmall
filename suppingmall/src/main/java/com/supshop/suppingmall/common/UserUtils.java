package com.supshop.suppingmall.common;

import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.SessionUser;

import javax.servlet.http.HttpSession;

public class UserUtils {

    public static boolean isSameUser(Long id, HttpSession session) {
        if(isSessionNull(session)) {
            return false;
        }
        SessionUser user = getSessionUser(session);
        if(user.getUserId().equals(id)) {
            return true;
        }
        return false;
    }

    public static boolean isSessionNull(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        if(user == null) {
            return true;
        }
        return false;
    }

    public static SessionUser getSessionUser(HttpSession session) {
        return (SessionUser) session.getAttribute("user");
    }

    public static boolean isAdmin(HttpSession session) {
        if(isSessionNull(session)) {
            return false;
        }
        SessionUser sessionUser = getSessionUser(session);
        if (sessionUser.getRole().equals(Role.MASTER) || sessionUser.getRole().equals(Role.MASTER)) {
            return true;
        }
        return false;
    }

    public boolean isLoginUser(SessionUser sessionUser) {
        if(sessionUser != null) return true;
        return false;
    }

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
