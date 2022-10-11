package com.bmarket.securityservice.exception.advisor;

import com.bmarket.securityservice.api.controller.external_spec.responseForm.ResponseForm;
import com.bmarket.securityservice.exception.ErrorResponse;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
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
        ResponseForm<ErrorResponse> response = new ResponseForm<>(ResponseStatus.ERROR, new ErrorResponse(e));
        return ResponseEntity.badRequest().body(response);
    }
}
