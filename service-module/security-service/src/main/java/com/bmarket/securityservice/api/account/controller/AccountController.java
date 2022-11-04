package com.bmarket.securityservice.api.account.controller;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;

import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.common.ResponseForm;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.exception.validate.RequestSignUpFormValidator;
import com.bmarket.securityservice.api.account.service.AccountQueryService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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

    private final RequestSignUpFormValidator requestSignUpFormValidator;
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_SIZE = "20";
    private final String DEFAULT_DIRECTION = "DESC";

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(requestSignUpFormValidator);
    }

    /**
     * POST : /ACCOUNT
     * :계정 생성 요청
     * @param form
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseForm> createAccount(@Valid @RequestBody RequestSignUpForm form, BindingResult bindingResult) {
        log.info("==============[CONTROLLER] 회원가입 요청=============");
        if (bindingResult.hasErrors()) {
            throw new BasicException(ErrorCode.FAIL_VALIDATION, bindingResult);
        }
        Link link = linkTo(methodOn(AccountController.class).createAccount(form, bindingResult)).withSelfRel();

        SignupResult result = accountCommandService.signUpProcessing(form);
        result.add(link);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setDate(new Date().getTime());

        return ResponseEntity
                .created(link.toUri())
                .headers(httpHeaders)
                .body(new ResponseForm(ResponseStatus.SUCCESS, result));
    }

    /**
     * GET : /ACCOUNT/{clientId}
     * 계정 단건 조회
     */
    @GetMapping("/{accountId}")
    public ResponseEntity findAccount(@PathVariable Long accountId) {
        FindOneAccountResult result = accountQueryService.findAccountDetail(accountId);


        result.add(linkTo(AccountController.class).withSelfRel());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setDate(new Date().getTime());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm<>(ResponseStatus.REQUEST_SUCCESS, result));
    }

    @GetMapping
    public ResponseEntity findAllAccount(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                         @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                         @RequestParam Authority authority) {

        PageRequest request = PageRequest.of(setPage(page), size, direction, "id");
        AccountListResult result = accountQueryService.findAccountList(request,authority);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setDate(new Date().getTime());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(new ResponseForm(ResponseStatus.SUCCESS, result));
    }

    public void updatePassword(){

    }

    public void deleteAccount(){

    }

    /**
     * PageRequest 은 0 번 부터 시작하므로 사용자 편의성을위해 1부터 검색할 수 있도록 세팅
     *
     * @param page
     * @return
     */
    private int setPage(int page) {
        page = page - 1;

        if (page <= 0) {
            log.info("0 or Less then 0 is just 0");
            page = 0;
        }
        return page;
    }


}
