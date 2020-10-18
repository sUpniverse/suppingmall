package com.supshop.suppingmall.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
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
@Order(GlobalRestControllerAdvice.ORDER+1)
@ControllerAdvice
public class GlobalControllerAdvice {


    /**
     * URL not found 에러 공통 처리 (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected String NoHandlerFoundException(NoHandlerFoundException e) {
        log.debug(e.getLocalizedMessage());
        return "/error/404";
    }

    /**
     * 잘못된 요청 정보로 에러가 났을 경우 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug(e.getLocalizedMessage());
        return "/error/400";
    }


    /**
     * @Validate 으로 검증 error 발생시 발생 => 다시 요청 페이지 +error로 전달
     */
    @ExceptionHandler(BindException.class)
    protected String handleBindException(BindException e,
                                                     HttpServletRequest request,
                                                     RedirectAttributes redirectAttributes) {
        log.debug(e.getLocalizedMessage());
        String referer = request.getHeader("Referer");
//        redirectAttributes.addAttribute("errorMsg", e.getMessage());

        return "redirect:"+referer+"?error";
    }


    /**
     * DB Connection Error (500)
     */
    @ExceptionHandler(SQLException.class)
    protected String  handleDataIntegrityViolationException(SQLException e) {
        log.error(e.getLocalizedMessage());
        return "/error/500";
    }


    /**
     * 그외 Error 발생 시 (500)
     */
    @ExceptionHandler(Exception.class)
    protected String handleException(Exception e) {
        log.debug(e.getLocalizedMessage());
        return "/error/500";
    }


}
