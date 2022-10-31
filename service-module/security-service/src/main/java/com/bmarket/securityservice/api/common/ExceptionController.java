package com.bmarket.securityservice.api.common;


import com.bmarket.securityservice.exception.ErrorResponse;
import com.bmarket.securityservice.exception.custom_exception.security_ex.CustomAccessDeniedException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.DeniedTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.EmptyTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.ExpiredTokenException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.net.URI;

import static com.bmarket.securityservice.utils.url.JwtEntrypointRedirectUrl.*;

@RestController
@Slf4j
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping(EMPTY_TOKEN)
    public ResponseEntity emptyTokenException(@RequestParam(defaultValue = "token") String token) {
        log.info("Empty Token Error 응답");
        ErrorCode errorCode = ErrorCode.EMPTY_ACCESS_TOKEN;

        if (token.equals("refresh")) {
            errorCode = ErrorCode.REFRESH_TOKEN_IS_EMPTY;
        }

        ErrorResponse errorResponse = new ErrorResponse(new EmptyTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping(EXPIRED_TOKEN)
    public ResponseEntity expiredTokenException() {
        log.info("Expired Token exception 응답");

        ErrorResponse errorResponse = new ErrorResponse(new ExpiredTokenException(ErrorCode.EXPIRED_REFRESH_TOKEN));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(URI.create("/login"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body(errorResponse);
    }

    @GetMapping(DENIED_TOKEN)
    public ResponseEntity deniedTokenException(@RequestParam String token) {
        log.info("deniedTokenException 응답");

        ErrorCode errorCode = ErrorCode.DENIED_ACCESS_TOKEN;
        if (token.equals("refresh")) {
            errorCode = ErrorCode.DENIED_REFRESH_TOKEN;
        }

        ErrorResponse errorResponse = new ErrorResponse(new DeniedTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/access-denied")
    public ResponseEntity accessDeniedException(){

        ErrorResponse errorResponse = new ErrorResponse(new CustomAccessDeniedException(ErrorCode.ACCESS_DENIED));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
