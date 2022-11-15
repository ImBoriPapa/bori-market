package com.bmarket.securityservice.domain.common;


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

import static com.bmarket.securityservice.utils.status.ResponseStatus.*;


@RestController
@Slf4j
@RequestMapping("/exception")
public class AuthenticationFailResponseController {
    @GetMapping("/empty-both")
    public ResponseEntity emptyAnyThing() {
        ResponseForm.Error errorResponse = new ResponseForm.Error(new CustomAccessDeniedException(MUST_NEED_TOKEN_AND_ID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/empty-clientId")
    public ResponseEntity emptyClientId() {

        ResponseForm.Error errorResponse = new ResponseForm.Error(new CustomAccessDeniedException(CLIENT_ID_IS_EMPTY));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/wrong-clientId")
    public ResponseEntity wrongClientId() {
        ResponseForm.Error errorResponse = new ResponseForm.Error(new CustomAccessDeniedException(CLIENT_ID_IS_INVALID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/empty-token")
    public ResponseEntity emptyTokenException(@RequestParam(defaultValue = "token") String token) {

        ResponseStatus errorCode = ACCESS_TOKEN_IS_EMPTY;

        if (token.equals("refresh")) {
            errorCode = REFRESH_TOKEN_IS_EMPTY;
        }

        ResponseForm.Error errorResponse = new ResponseForm.Error(new EmptyTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/expired-token")
    public ResponseEntity expiredTokenException() {

        ResponseForm.Error errorResponse = new ResponseForm.Error(new ExpiredTokenException(REFRESH_TOKEN_IS_EXPIRED));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(URI.create("/login"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body(errorResponse);
    }

    @GetMapping("/denied-token")
    public ResponseEntity deniedTokenException(@RequestParam String token) {

        ResponseStatus errorCode = ACCESS_TOKEN_IS_DENIED;
        if (token.equals("refresh")) {
            errorCode = REFRESH_TOKEN_IS_DENIED;
        }

        ResponseForm.Error errorResponse = new ResponseForm.Error(new DeniedTokenException(errorCode));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/access-denied")
    public ResponseEntity accessDeniedException() {

        ResponseForm.Error errorResponse = new ResponseForm.Error(new CustomAccessDeniedException(ACCESS_DENIED));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
