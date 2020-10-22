package com.supshop.suppingmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws AccessDeniedException, ServletException, IOException {
        log.debug(e.getLocalizedMessage());
        response.setStatus(403);
        if(isJsonRequest(request)) {
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return;
        }
        request.getRequestDispatcher("/error/403").forward(request, response);
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();

        if(contentType != null && (contentType.equals(MediaType.APPLICATION_JSON)
                || contentType.equals(MediaType.APPLICATION_JSON_VALUE)
                || contentType.equals(MediaType.APPLICATION_JSON_UTF8)
                || contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            return true;

        return false;
    }
}
