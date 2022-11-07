package com.bmarket.securityservice.api.common;


import com.bmarket.securityservice.exception.custom_exception.security_ex.CustomAccessDeniedException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.DeniedTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.EmptyTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.ExpiredTokenException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
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
        ResponseStatus errorCode = ResponseStatus.EMPTY_ACCESS_TOKEN;

        if (token.equals("refresh")) {
            errorCode = ResponseStatus.REFRESH_TOKEN_IS_EMPTY;
        }

        ResponseForm.ErrorResponse errorResponse = new ResponseForm.ErrorResponse(new EmptyTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping(EXPIRED_TOKEN)
    public ResponseEntity expiredTokenException() {
        log.info("Expired Token exception 응답");

        ResponseForm.ErrorResponse errorResponse = new ResponseForm.ErrorResponse(new ExpiredTokenException(ResponseStatus.EXPIRED_REFRESH_TOKEN));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(URI.create("/login"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body(errorResponse);
    }

    @GetMapping(DENIED_TOKEN)
    public ResponseEntity deniedTokenException(@RequestParam String token) {
        log.info("deniedTokenException 응답");

        ResponseStatus errorCode = ResponseStatus.DENIED_ACCESS_TOKEN;
        if (token.equals("refresh")) {
            errorCode = ResponseStatus.DENIED_REFRESH_TOKEN;
        }

        ResponseForm.ErrorResponse errorResponse = new ResponseForm.ErrorResponse(new DeniedTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/access-denied")
    public ResponseEntity accessDeniedException(){

        ResponseForm.ErrorResponse errorResponse = new ResponseForm.ErrorResponse(new CustomAccessDeniedException(ResponseStatus.ACCESS_DENIED));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
