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


@RestController
@Slf4j
@RequestMapping("/exception")
public class AuthenticationFailResponseController {
    @GetMapping("/empty-both")
    public ResponseEntity emptyAnyThing(@RequestParam String clientId) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ooo");
    }

    @GetMapping("/empty-clientId")
    public ResponseEntity emptyClientId(@RequestParam String clientId) {


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ooo");
    }

    @GetMapping("/wrong-clientId")
    public ResponseEntity wrongClientId(@RequestParam String clientId) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ooo");
    }

    @GetMapping("/empty-token")
    public ResponseEntity emptyTokenException(@RequestParam(defaultValue = "token") String token) {

        ResponseStatus errorCode = ResponseStatus.EMPTY_ACCESS_TOKEN;

        if (token.equals("refresh")) {
            errorCode = ResponseStatus.REFRESH_TOKEN_IS_EMPTY;
        }

        ResponseForm.Error errorResponse = new ResponseForm.Error(new EmptyTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/expired-token")
    public ResponseEntity expiredTokenException() {

        ResponseForm.Error errorResponse = new ResponseForm.Error(new ExpiredTokenException(ResponseStatus.EXPIRED_REFRESH_TOKEN));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(URI.create("/login"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body(errorResponse);
    }

    @GetMapping("/denied-token")
    public ResponseEntity deniedTokenException(@RequestParam String token) {


        ResponseStatus errorCode = ResponseStatus.INVALID_ACCESS_TOKEN;
        if (token.equals("refresh")) {
            errorCode = ResponseStatus.INVALID_REFRESH_TOKEN;
        }

        ResponseForm.Error errorResponse = new ResponseForm.Error(new DeniedTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/access-denied")
    public ResponseEntity accessDeniedException() {

        ResponseForm.Error errorResponse = new ResponseForm.Error(new CustomAccessDeniedException(ResponseStatus.ACCESS_DENIED));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
