package com.bmarket.securityservice.domain.security.controller;

import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.domain.security.controller.requestForm.RequestLoginForm;
import com.bmarket.securityservice.domain.security.controller.requestResultForm.LoginResultForm;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;

    // TODO: 2022/11/21  로그아웃 기능 구현 링크 추가
    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody RequestLoginForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            throw new FormValidationException(ResponseStatus.FAIL_LOGIN);
        }

        LoginResult result = jwtService.loginProcessing(form.getLoginId(), form.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID,result.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, result.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, result.getRefreshToken());

        EntityModel<LoginResultForm> of = EntityModel.of(new LoginResultForm(result.getAccountId(), result.getLoginAt()));

        of.add(WebMvcLinkBuilder.linkTo(LoginController.class).withSelfRel());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, of));
    }
}
