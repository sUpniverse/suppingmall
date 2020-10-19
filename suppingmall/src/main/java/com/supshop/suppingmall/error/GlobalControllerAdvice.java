package com.supshop.suppingmall.error;

import com.supshop.suppingmall.error.exception.order.InvalidConfirmationTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 에러 공동처리 영역
 * Controller(HTML)에서 발생되는 에러를 공동처리 한다.
 */

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {


    /**
     * URL not found 에러 공통 처리 (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected Object handleNoHandlerFoundException(NoHandlerFoundException e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJson(request)){
            return ResponseEntity.notFound().build();
        }
        return "/error/404";
    }

    private boolean isJson(HttpServletRequest request) {
        String contentType = request.getContentType();

        if(contentType != null && (contentType.equals(MediaType.APPLICATION_JSON)
                || contentType.equals(MediaType.APPLICATION_JSON_VALUE)
                || contentType.equals(MediaType.APPLICATION_JSON_UTF8)
                || contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            return true;

        return false;
    }

    /**
     * 잘못된 요청 정보로 에러가 났을 경우 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJson(request)){
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }

    /**
     * 잘못된 토큰 정보 요청 (400)
     */
    @ExceptionHandler(InvalidConfirmationTokenException.class)
    public Object handleInvalidConfirmationTokenException(InvalidConfirmationTokenException e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJson(request)){
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }

    /**
     * DB의 제약조건 에러 (400)
     */
    @ExceptionHandler(DataAccessException.class)
    protected Object handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJson(request)) {
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }


    /**
     * @Validate 으로 검증 error 발생시 발생 => 다시 요청 페이지 +error로 전달
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                           HttpServletRequest request,
                                                           RedirectAttributes redirectAttributes) {
        log.debug(e.getLocalizedMessage());
        String referer = request.getHeader("Referer");
//        redirectAttributes.addAttribute("errorMsg", e.getMessage());
        if(isJson(request)) {
            return ResponseEntity.badRequest().build();
        }

        return "redirect:"+referer+"?error";
    }

    /**
     * @ModelAttribute binding error 발생시 발생 => 다시 요청 페이지 +error로 전달
     */
    @ExceptionHandler(BindException.class)
    protected Object handleBindException(BindException e,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        log.debug(e.getLocalizedMessage());
        String referer = request.getHeader("Referer");
//        redirectAttributes.addAttribute("errorMsg", e.getMessage());
        if(isJson(request)) {
            return ResponseEntity.badRequest().build();
        }

        return "redirect:"+referer+"?error";
    }


    /**
     * DB Connection Error (500)
     */
    @ExceptionHandler(SQLException.class)
    protected Object  handleDataIntegrityViolationException(SQLException e,HttpServletRequest request) {
        log.error(e.getLocalizedMessage());
        if(isJson(request)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return "/error/500";
    }


    /**
     * 그외 Error 발생 시 (500)
     */
    @ExceptionHandler(Exception.class)
    protected Object handleException(Exception e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJson(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return "/error/500";
    }


}
