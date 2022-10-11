package com.bmarket.securityservice.api.controller;

import com.bmarket.securityservice.api.controller.external_spec.responseForm.ResponseForm;
import com.bmarket.securityservice.api.dto.LoginResult;
import com.bmarket.securityservice.domain.service.LoginService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {

        LoginResult result = loginService.login(loginDto.loginId, loginDto.password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",result.getToken());
        httpHeaders.add("Refresh",result.getRefreshToken());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm<>(ResponseStatus.SUCCESS,new LoginValue(result)));
    }

    @Data
    public static class LoginDto {
        private String loginId;
        private String password;
    }

    @Data
    public static class LoginValue {
        private String clientId;
        private LocalDateTime loginTime;

        public LoginValue(LoginResult result) {
            this.clientId = result.getClientId();
            this.loginTime = result.getLoginAt();
        }
    }
}
