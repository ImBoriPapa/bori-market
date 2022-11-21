package com.bmarket.securityservice.exception.validator;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class AccountDuplicateValidator {
    private final AccountRepository accountRepository;
    @Transactional(readOnly = true)
    public void validate(RequestAccountForm.CreateForm form) {
        log.info("[ACCOUNT FORM VALIDATOR 동작]");
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
