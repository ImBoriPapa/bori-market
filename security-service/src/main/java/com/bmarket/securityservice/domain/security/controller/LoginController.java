package com.bmarket.securityservice.domain.security.controller;

import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.security.controller.requestForm.RequestLoginForm;
import com.bmarket.securityservice.domain.security.controller.requestResultForm.LoginResultForm;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.SecurityHeader.REFRESH_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {


    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody RequestLoginForm form) {

        LoginResult result = jwtService.loginProcessing(form.getLoginId(), form.getPassword());
        LoginResultForm resultForm = new LoginResultForm(result.getLoginAt());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, result.getAccessToken());
        httpHeaders.add(REFRESH_HEADER, result.getRefreshToken());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, resultForm));
    }
}
