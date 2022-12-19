package com.bmarket.securityservice.account.api;

import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.AccountQueryRepository;
import com.bmarket.securityservice.account.domain.repository.dto.AccountList;
import com.bmarket.securityservice.account.domain.repository.dto.AccountListResult;
import com.bmarket.securityservice.account.domain.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.account.domain.service.AccountCommandService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.exception.validator.AccountDuplicateValidator;
import com.bmarket.securityservice.security.api.LoginController;
import com.bmarket.securityservice.utils.LinkProvider;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final AccountCommandService accountCommandService;
    private final AccountQueryRepository queryRepository;
    private final AccountDuplicateValidator accountDuplicateValidator;
    private final LinkProvider linkProvider;

    /**
     * POST : /ACCOUNT
     * 계정 생성 요청
     */
    @PostMapping
    public ResponseEntity<ResponseForm.Of> createAccount(
            @Validated
            @RequestBody RequestAccountForm.CreateForm form, BindingResult bindingResult,
            HttpServletRequest request) {

        log.info("[ACCOUNT CONTROLLER createAccount]");

        if (bindingResult.hasErrors()) {
            log.error("Validation Error 발생");
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        accountDuplicateValidator.validate(form);

        WebMvcLinkBuilder link = linkTo(methodOn(AccountController.class).createAccount(form, bindingResult, request));
        Account account = accountCommandService.signUpProcessing(form);

        ResponseAccountForm.ResponseSignupForm result = new ResponseAccountForm.ResponseSignupForm(account.getId(), account.getCreatedAt());

        URI Location = link.slash(result.getAccountId()).toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        EntityModel<ResponseAccountForm.ResponseSignupForm> entityModel = EntityModel.of(result);
        entityModel.add(linkProvider.createOneLink(LoginController.class, "login", "POST : 로그인"));

        return ResponseEntity
                .created(Location)
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * GET : /ACCOUNT/{accountId}
     * 계정 단건 조회
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ResponseForm.Of> getAccount(@PathVariable Long accountId) {

        List<Link> crudLink = linkProvider.createCrudLink(AccountController.class, accountId, "계정");

        FindOneAccountResult result = queryRepository.findOneAccount(accountId)
                .orElseThrow(() -> new IllegalArgumentException("조회 결과가 없습니다."));

        EntityModel<FindOneAccountResult> entityModel = EntityModel.of(result)
                .add(crudLink);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    // TODO: 2022/11/21 admin 기능으로 변경하기

    /**
     * 계정 다건 조회
     */
    @GetMapping
    public ResponseEntity<ResponseForm.Of> getAccountList(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Authority authority) {

        Page<AccountList> list = queryRepository.findAccountListByPageable(pageable, authority);

        AccountListResult result = new AccountListResult(list);

        EntityModel<AccountListResult> entityModel = EntityModel.of(result);
        entityModel.add(linkProvider.createPageLink(AccountController.class, result.getPageNumber(), result.getSize(), result.getTotalCount()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity deleteAccount(@Validated @PathVariable Long accountId, @RequestBody RequestAccountForm.DeleteForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("error={}", bindingResult);
        }

        accountCommandService.deleteAccount(accountId, form.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseAccountForm.ResponseResultForm deleteForm = new ResponseAccountForm.ResponseResultForm(accountId);
        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(deleteForm);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    @PutMapping("/{accountId}/password")
    public ResponseEntity updatePassword(@PathVariable Long accountId,
                                         @RequestBody RequestAccountForm.UpdatePasswordForm form) {

        accountCommandService.updatePassword(accountId, form.getPassword(), form.getNewPassword());

        ResponseAccountForm.ResponseResultForm passwordForm = new ResponseAccountForm.ResponseResultForm(accountId);
        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(passwordForm);
        Link loginLink = linkProvider.createOneLink(LoginController.class, "login", "login");
        entityModel.add(loginLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    @PutMapping("/{accountId}/email")
    public ResponseEntity updateEmail(@PathVariable Long accountId,
                                      @RequestBody RequestAccountForm.UpdateEmailForm form) {
        log.info("[UpdateEmail param= '{}']", form.getEmail());
        accountCommandService.updateEmail(accountId, form.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseAccountForm.ResponseResultForm resultForm = new ResponseAccountForm.ResponseResultForm(accountId);

        EntityModel<ResponseAccountForm.ResponseResultForm> entityModel = EntityModel.of(resultForm);

        Link loginLink = linkProvider.createOneLink(LoginController.class, "login", "login");
        entityModel.add(loginLink);

        return ResponseEntity.ok().headers(headers).body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }
}
