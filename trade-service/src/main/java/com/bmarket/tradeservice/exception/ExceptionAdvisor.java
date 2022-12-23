package com.bmarket.tradeservice.exception;

import com.bmarket.tradeservice.api.responseForm.ResponseForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleException(Exception ex, WebRequest request) {
        log.info("[IllegalArgumentException 발생 메세지는 ={}]", ex.getMessage());

        return ResponseEntity
                .internalServerError()
                .body(new ResponseForm.Error(ex, request.getDescription(false)));
    }
}
