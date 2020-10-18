package com.supshop.suppingmall.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache  = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private static final String defaultUrl = "/";
    private static final String prevPageContext = "prevPage";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        getPreviousUrl(httpServletRequest, httpServletResponse, authentication);
    }

    /**
     * 로그인이 필요한 요청을 받아 로그인 페이지에서
     * 로그인을 한 후 요청했던 페이지로 다시 돌아감
     * session에 prev페이지를 넣어놓아서 그걸 읽어서 redirect 해줌
     * @param request
     * @param response
     * @param authentication
     */
    private void getPreviousUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String prevPage = (String) request.getSession().getAttribute(prevPageContext);
        String url = defaultUrl;

        if(savedRequest != null) {
            url = savedRequest.getRedirectUrl();
        } else if(prevPage != null) {
            request.getSession().removeAttribute(prevPageContext);
            url = prevPage;
        }
        redirectStrategy.sendRedirect(request, response, url);
    }
}
