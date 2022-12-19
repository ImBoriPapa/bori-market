package com.bmarket.securityservice.exception.advisor;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FailAuthenticationException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception e) {
        log.info("[ExceptionAdvisor: 예상하지 못한 에러입니다. Message={}]",e.getMessage());
        BasicException basicException = new BasicException(ResponseStatus.ERROR);
        ResponseForm.Error errorResponse = new ResponseForm.Error(basicException);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BasicException.class)
    public ResponseEntity basic(BasicException e) {
        log.info("[ExceptionAdvisor: Basic Exception 에러입니다. Message={}]",e.getMessage());
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(FailAuthenticationException.class)
    public ResponseEntity failAuthentication(FailAuthenticationException e) {
        log.info("[ExceptionAdvisor: failAuthentication Exception 에러입니다. Message={}]",e.getMessage());
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(FormValidationException.class)
    public ResponseEntity validation(FormValidationException e) {
        log.info("[ExceptionAdvisor: validation Exception 에러입니다. Message={}]",e.getMessage());
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(NotFoundAccountException.class)
    public ResponseEntity notFoundAccount(NotFoundAccountException e){
        log.info("[ExceptionAdvisor: notFoundAccount Exception 에러입니다. Message={}]",e.getMessage());
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
