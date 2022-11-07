package com.bmarket.securityservice.exception.validate;

import com.bmarket.securityservice.api.account.controller.RequestAccountForm;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateSignupFormValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestAccountForm.CreateForm.class);
    }
    @Transactional(readOnly = true)
    @Override
    public void validate(Object target, Errors errors) {
        log.info("[ACCOUNT FORM 검증기 동작]");

        RequestAccountForm.CreateForm form = (RequestAccountForm.CreateForm) target;

        if (accountRepository.existsByLoginId(form.getLoginId())) {
            throw new BasicException(ResponseStatus.ERROR);
        }

        if (accountRepository.existsByEmail(form.getLoginId())) {
            throw new BasicException(ResponseStatus.DUPLICATE_EMAIL);
        }

        if (accountRepository.existsByContact(form.getLoginId())) {
            throw new BasicException(ResponseStatus.DUPLICATE_CONTACT);
        }
    }
}
