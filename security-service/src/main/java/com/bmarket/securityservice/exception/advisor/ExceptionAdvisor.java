package com.bmarket.securityservice.exception.advisor;

import com.bmarket.securityservice.api.common.ResponseForm;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FailAuthenticationException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    public void exception(Exception e) {
        log.error("예상하지 못한 에러 발생 ", e);
    }

    @ExceptionHandler(BasicException.class)
    public ResponseEntity basic(BasicException e) {
        log.info("BASIC EXCEPTION 햰들러 작동");
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(FailAuthenticationException.class)
    public ResponseEntity failAuthenticationException(FailAuthenticationException e) {
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(FormValidationException.class)
    public ResponseEntity validation(FormValidationException e){
        log.info("VALIDATION EXCEPTION 햰들러 작동");
        ResponseForm.Error errorResponse = new ResponseForm.Error(e);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
