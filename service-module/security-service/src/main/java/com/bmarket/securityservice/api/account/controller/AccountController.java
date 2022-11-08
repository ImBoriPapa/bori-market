package com.bmarket.securityservice.api.account.controller;

import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.common.ResponseForm;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.security.controller.LoginController;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.exception.validator.CreateSignupFormValidator;
import com.bmarket.securityservice.api.account.service.AccountQueryService;
import com.bmarket.securityservice.utils.LinkProvider;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final AccountQueryService accountQueryService;
    private final AccountCommandService accountCommandService;
    private final CreateSignupFormValidator createSignupFormValidator;
    private final LinkProvider linkProvider;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(createSignupFormValidator);
    }

    /**
     * POST : /ACCOUNT
     * 계정 생성 요청
     *
     * @param form
     * @return ResponseForm
     */
    @PostMapping
    public ResponseEntity<ResponseForm<EntityModel>> createAccount(
            @Validated
            @RequestBody RequestAccountForm.CreateForm form, BindingResult bindingResult) {
        log.info("[ACCOUNT CONTROLLER] createAccount");

        if (bindingResult.hasErrors()) {
            log.info("Validation Error 발생");
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        WebMvcLinkBuilder link = linkTo(methodOn(AccountController.class).createAccount(form, bindingResult));

        ResponseAccountForm.ResponseSignupForm result = accountCommandService.signUpProcessing(form);

        URI Location = link.slash(result.getAccountId()).toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        EntityModel<ResponseAccountForm.ResponseSignupForm> entityModel = EntityModel.of(result);
        entityModel.add(linkProvider.createOneLink(LoginController.class, "login", "POST : 로그인"));
        entityModel.add(linkProvider.createCrudLink(AccountController.class, result.getAccountId(), "계정"));

        return ResponseEntity
                .created(Location)
                .headers(httpHeaders)
                .body(new ResponseForm<>(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * GET : /ACCOUNT/{accountId}
     * 계정 단건 조회
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ResponseForm<EntityModel<FindOneAccountResult>>> getAccount(@PathVariable Long accountId ) {

        FindOneAccountResult result = accountQueryService.findAccountDetail(accountId);

        List<Link> crudLink = linkProvider.createCrudLink(AccountController.class, accountId, "계정");

        EntityModel<FindOneAccountResult> entityModel = EntityModel.of(result)
                .add(crudLink);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm<>(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 계정 다건 조회
     *
     * @param authority
     * @return
     */
    @GetMapping
    public ResponseEntity<ResponseForm<EntityModel<AccountListResult>>> getAccountList(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Authority authority) {

        AccountListResult result = accountQueryService.findAccountList(pageable, authority);

        EntityModel<AccountListResult> entityModel = EntityModel.of(result);
        entityModel.add(linkProvider.createPageLink(AccountController.class, result.getPageNumber(), result.getSize(), result.getTotalCount()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm<>(ResponseStatus.SUCCESS, entityModel));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable Long accountId, @RequestBody RequestAccountForm.DeleteForm form) {

        accountCommandService.deleteAccount(accountId, form.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseAccountForm.ResponseResultForm deleteForm = new ResponseAccountForm.ResponseResultForm("ok");
        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(deleteForm);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseForm<>(ResponseStatus.SUCCESS, entityModel));
    }

    @PutMapping("/{accountId}/password")
    public ResponseEntity updatePassword(@PathVariable Long accountId,
                               @RequestBody RequestAccountForm.UpdatePasswordForm form) {

        accountCommandService.updatePassword(accountId,form.getPassword(),form.getNewPassword());

        ResponseAccountForm.ResponseResultForm passwordForm = new ResponseAccountForm.ResponseResultForm("비밀번호 변경 성공");
        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(passwordForm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }

    @PutMapping("/{accountId}/email")
    public ResponseEntity updateEmail(@PathVariable Long accountId,
                            @RequestBody RequestAccountForm.UpdateEmailForm form){
        log.info("[UpdateEmail param= '{}']",form.getEmail());
        accountCommandService.updateEmail(accountId,form.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseAccountForm.ResponseResultForm resultForm = new ResponseAccountForm.ResponseResultForm("OK");
        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(resultForm);

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }

    


}
