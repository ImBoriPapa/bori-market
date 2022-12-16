package com.bmarket.securityservice.security.api;

import com.bmarket.securityservice.account.api.AccountController;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.security.api.requestForm.RequestLoginForm;
import com.bmarket.securityservice.security.api.requestResultForm.ResponseRefresh;
import com.bmarket.securityservice.security.api.requestResultForm.LoginResultForm;
import com.bmarket.securityservice.security.service.JwtService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.SecurityHeader.REFRESH_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<ResponseForm.Of> login(@Validated @RequestBody RequestLoginForm form, BindingResult bindingResult) {
        log.info("[LOGIN CONTROLLER]");

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_LOGIN);
        }

        LoginResult result = jwtService.loginProcessing(form.getEmail(), form.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AUTHORIZATION_HEADER, result.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, result.getRefreshToken());

        EntityModel<LoginResultForm> resultOf = EntityModel.of(new LoginResultForm(result.getAccountId(), result.getLoginAt()));

        resultOf.add(WebMvcLinkBuilder.linkTo(AccountController.class).slash(result.getAccountId()).withRel("GET : 계정 정보 조회"));


        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, resultOf));
    }

    @GetMapping("/refresh/{token}")
    public ResponseEntity refreshCheck(@PathVariable String token) {

        log.info("token={}", token);
//        CheckRefreshResult checkRefreshResult = jwtService.reissueRefreshToken(token);
        ResponseRefresh responseRefresh1 = new ResponseRefresh("dadsdsa","dsadsdas");
        return ResponseEntity.ok().body(responseRefresh1);
    }

}
