package com.bmarket.frmservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity ex(IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(new ResponseError(false, ex.getMessage()));
    }
}
