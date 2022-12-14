package com.bmarket.tradeservice.domain.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisor {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity error(IllegalArgumentException ex){
        log.info("[IllegalArgumentException 발생 메세지는 ={}]",ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @NoArgsConstructor
    @Getter
    public static class ErrorResponse{
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
