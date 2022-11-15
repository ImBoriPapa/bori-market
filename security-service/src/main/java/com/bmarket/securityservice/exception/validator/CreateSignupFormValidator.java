package com.bmarket.securityservice.exception.validator;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
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
        log.info("[ACCOUNT FORM VALIDATOR 동작]");

        RequestAccountForm.CreateForm form = (RequestAccountForm.CreateForm) target;

        if (accountRepository.existsByLoginId(form.getLoginId())) {
            throw new FormValidationException(ResponseStatus.DUPLICATE_LOGIN_ID);
        }

        if (accountRepository.existsByEmail(form.getEmail())) {
            throw new FormValidationException(ResponseStatus.DUPLICATE_EMAIL);
        }

        if (accountRepository.existsByContact(form.getContact())) {
            throw new FormValidationException(ResponseStatus.DUPLICATE_CONTACT);
        }
    }
}
