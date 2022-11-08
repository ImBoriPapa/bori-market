package com.bmarket.securityservice.api.security.controller;

import com.bmarket.securityservice.api.common.ResponseForm;
import com.bmarket.securityservice.api.security.controller.requestForm.RequestLoginForm;
import com.bmarket.securityservice.api.security.controller.requestResultForm.LoginResultForm;
import com.bmarket.securityservice.api.security.service.LoginService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.bmarket.securityservice.utils.jwt.JwtHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.JwtHeader.REFRESH_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody RequestLoginForm form) {

        LoginResult result = loginService.loginProcessing(form.getLoginId(), form.getPassword());
        LoginResultForm resultForm = new LoginResultForm(result.getLoginAt());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, result.getToken());
        httpHeaders.add(REFRESH_HEADER, result.getRefreshToken());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, resultForm));
    }
}
