package com.bmarket.securityservice.security.api;

import com.bmarket.securityservice.account.api.AccountController;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.TokenException;
import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.security.api.reponseForm.ReIssueResultForm;
import com.bmarket.securityservice.security.api.requestForm.RequestLoginForm;
import com.bmarket.securityservice.security.api.dto.ReIssueResult;
import com.bmarket.securityservice.security.api.reponseForm.LoginResultForm;
import com.bmarket.securityservice.security.service.JwtService;
import com.bmarket.securityservice.utils.JwtUtils;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.bmarket.securityservice.security.constant.SecurityHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.security.constant.SecurityHeader.REFRESH_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;

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

        EntityModel<LoginResultForm> resultOf = EntityModel.of(new LoginResultForm(result.getMemberId(), result.getLoginAt()));

        resultOf.add(WebMvcLinkBuilder.linkTo(AccountController.class).slash(result.getMemberId()).withRel("GET : 계정 정보 조회"));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, resultOf));
    }

    @GetMapping("/refresh/{memberId}")
    public ResponseEntity reIssueTokenRequest(@PathVariable String memberId, HttpServletRequest request) {
        log.info("[REISSUE TOKEN REQUEST]");

        String refreshToken = jwtUtils.resolveToken(request, REFRESH_HEADER)
                .orElseThrow(() -> new TokenException(ResponseStatus.REFRESH_TOKEN_IS_EMPTY));

        log.info("[MEMBER ID= {}]", memberId);
        log.info("[REFRESH TOKEN = {}]", refreshToken);

        ReIssueResult result = jwtService.reissueTokenProcessing(memberId, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, result.getAccessToken());
        headers.set(REFRESH_HEADER, result.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(new ReIssueResultForm(result.getMemberId(), result.getAccessTokenExpiredAt()));
    }
}
