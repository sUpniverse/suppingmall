package com.supshop.suppingmall.api.common;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrappingAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> c) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter mp, MediaType t,
                                  Class<? extends HttpMessageConverter<?>> c,
                                  ServerHttpRequest req, ServerHttpResponse res) {
        if (body instanceof ApiResponse<?> || body instanceof String || body instanceof byte[]) return body;
        String traceId = MDC.get("traceId");
        return ApiResponse.ok(body, traceId);
    }
}