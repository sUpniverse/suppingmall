package com.supshop.suppingmall.error;

import com.supshop.suppingmall.error.exception.BusinessException;
import com.supshop.suppingmall.error.exception.order.InvalidConfirmationTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 에러 공동처리 영역
 * Controller에서 발생되는 공동에러를 처리 한다.
 */

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {


    /**
     * URL not found 에러 공통 처리 (404)
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected Object handleNoHandlerFoundException(NoHandlerFoundException e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)){
            return ResponseEntity.notFound().build();
        }
        return "/error/404";
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

    /**
     * 잘못된 요청 정보로 에러가 났을 경우 (400)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)){
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
        if(isJsonRequest(request)){
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }

    /**
     * DB의 제약조건 에러 (400)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataAccessException.class)
    protected Object handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)) {
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }


    /**
     * 검증에러 (400)
     * @Validate 으로 검증 error 발생시 발생 => 다시 요청 페이지 +error로 전달
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                           HttpServletRequest request,
                                                           RedirectAttributes redirectAttributes) {
        log.debug(e.getLocalizedMessage());
        String referer = request.getHeader("Referer");
//        redirectAttributes.addAttribute("errorMsg", e.getMessage());
        if(isJsonRequest(request)) {
            return ResponseEntity.badRequest().build();
        }

        return "redirect:"+referer+"?error";
    }

    /**
     * 검증에러 (400)
     * @ModelAttribute binding error 발생시 발생 => 다시 요청 페이지 +error로 전달
     */
    @ExceptionHandler(BindException.class)
    protected Object handleBindException(BindException e,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        log.debug(e.getLocalizedMessage());
        String referer = request.getHeader("Referer");
//        redirectAttributes.addAttribute("errorMsg", e.getMessage());
        if(isJsonRequest(request)) {
            return ResponseEntity.badRequest().build();
        }

        return "redirect:"+referer+"?error";
    }

    /**
     * 권한이 없는 유저 (403)
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected Object handleAccessDeniedException(AccessDeniedException e,
                                                 HttpServletRequest request) {

        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getLocalizedMessage());
        }
        return "/error/403";
    }


    /**
     * DB Connection Error (500)
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    protected Object  handleDataIntegrityViolationException(SQLException e,
                                                            HttpServletRequest request) {
        log.error(e.getLocalizedMessage());
        if(isJsonRequest(request)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return "/error/500";
    }

    /**
     * SuppingMall에서 발생되는 요구사항에 맞지 않는 모든 에러 처리 (400)
     * 예) 이미 존재하는 아아템,유저 등등 해당 Exception을 상속하는 모든 에러 공통처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    protected Object handleBusinessException(BusinessException e,
                                             HttpServletRequest request){
        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)){
            return ResponseEntity.badRequest().build();
        }
        return "/error/400";
    }


    /**
     * 그외 Error 발생 시 (500)
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected Object handleException(Exception e,HttpServletRequest request) {
        log.debug(e.getLocalizedMessage());
        if(isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return "/error/500";
    }


}
