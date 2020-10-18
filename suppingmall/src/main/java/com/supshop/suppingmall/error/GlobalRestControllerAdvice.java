package com.supshop.suppingmall.error;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

import static com.supshop.suppingmall.error.GlobalRestControllerAdvice.ORDER;

@Slf4j
//@Order(ORDER)
//@RestControllerAdvice
public class GlobalRestControllerAdvice {

    public static final int ORDER = 0;

//    /**
//     * URL not found 에러 공통 처리 (404)
//     */
//    @ExceptionHandler(NoHandlerFoundException.class)
//    protected ResponseEntity NoHandlerFoundException(NoHandlerFoundException e) {
//        log.debug(e.getLocalizedMessage());
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 잘못된 parameter를 이용해 요청시 에러가 났을 경우
//     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
//        log.debug(e.getLocalizedMessage());
//        return ResponseEntity.badRequest().build();
//    }
//
//
//    /**
//     * @Validate 으로 검증 error 발생시 발생 (400)
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<Response> handleArgumentNotValidException(MethodArgumentNotValidException e) {
//        this.setLogger(e);
//        Response response = new Response(ResponseCode.INVALID_PARAMETER_TYPE.getDetailCode(), ResponseCode.INVALID_PARAMETER_TYPE.getReasonPhrase(), e.getMessage());
//        return new ResponseEntity<>(response, ResponseCode.INVALID_PARAMETER_TYPE.getHttpStatusCode());
//    }
//
//
//    /**
//     * 지원하지 않은 HTTP method 호출 할 경우 발생 (405)
//     */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    protected ResponseEntity<Response>  handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        this.setLogger(e);
//        Response response = new Response(ResponseCode.METHOD_NOT_ALLOWED.getDetailCode(), ResponseCode.METHOD_NOT_ALLOWED.getReasonPhrase(), e.getMessage());
//        return new ResponseEntity<>(response, ResponseCode.METHOD_NOT_ALLOWED.getHttpStatusCode());
//    }
//
//    /**
//     * Json Parsing 에러가 날 경우 (400)
//     */
//    @ExceptionHandler(JsonParseException.class)
//    protected ResponseEntity<Response>  JsonParseExceptionException(JsonParseException e) {
//        this.setLogger(e);
//        Response response = new Response(ResponseCode.BAD_REQUEST.getDetailCode(), ResponseCode.BAD_REQUEST.getReasonPhrase(), e.getMessage());
//        return new ResponseEntity<>(response, ResponseCode.BAD_REQUEST.getHttpStatusCode());
//    }


//    /**
//     * DB의 제약조건 에러 (400)
//     */
//    @ExceptionHandler(DataAccessException.class)
//    protected ResponseEntity  DataIntegrityViolationException(DataAccessException e) {
//        log.debug(e.getLocalizedMessage());
//        return ResponseEntity.badRequest().build();
//    }
//
//
//    /**
//     * DB Connection Error (500)
//     */
//    @ExceptionHandler(SQLException.class)
//    protected ResponseEntity  handleDataIntegrityViolationException(SQLException e) {
//        log.error(e.getLocalizedMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//
//    /**
//     * 그외 Error 발생 시 (500)
//     */
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<Response> handleException(Exception e) {
//        this.setLogger(e);
//        Response response = new Response(ResponseCode.INTERNAL_SERVER_ERROR.getDetailCode(), ResponseCode.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
//        return new ResponseEntity<>(response, ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatusCode());
//    }

}
