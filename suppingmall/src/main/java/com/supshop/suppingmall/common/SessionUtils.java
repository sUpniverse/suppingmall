package com.supshop.suppingmall.common;

import com.supshop.suppingmall.user.UserVO;
import org.apache.ibatis.javassist.NotFoundException;

import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static boolean isSameUser(Long id, HttpSession session) {
        if(isSessionNull(session)) {
            return false;
        }
        UserVO user = getSessionUser(session);
        if(user.getUserId().equals(id)) {
            return true;
        }
        return false;
    }

    public static boolean isSessionNull(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("user");
        if(user == null) {
            return true;
        }
        return false;
    }

    public static UserVO getSessionUser(HttpSession session) {
        return (UserVO) session.getAttribute("user");
    }
}
