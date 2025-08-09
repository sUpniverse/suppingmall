package com.supshop.suppingmall.api.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String traceId = req.getHeader("X-Request-Id");
        if (!StringUtils.hasText(traceId)) traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        res.setHeader("X-Request-Id", traceId);
        try {
            chain.doFilter(req, res);
        } finally {
            long took = System.currentTimeMillis() - start;
            log.info("[{}] {} {} -> {} ({}ms)", traceId, req.getMethod(), req.getRequestURI(), res.getStatus(), took);
            MDC.remove("traceId");
        }
    }
}