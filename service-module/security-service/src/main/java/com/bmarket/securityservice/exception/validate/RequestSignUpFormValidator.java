package com.bmarket.securityservice.exception.validate;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RequestSignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestSignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestSignUpForm form = (RequestSignUpForm) target;

        if (accountRepository.existsByLoginId(form.getLoginId())) {
            throw new BasicException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

//        if (accountRepository.existsByNickname(form.getNickname())) {
//            throw new BasicException(ErrorCode.DUPLICATE_NICKNAME);
//        }
//
//        if (accountRepository.existsByEmail(form.getEmail())) {
//            throw new BasicException(ErrorCode.DUPLICATE_EMAIL);
//        }
//
//        if (accountRepository.existsByContact(form.getContact())) {
//            throw new BasicException(ErrorCode.DUPLICATE_CONTACT);
//        }
    }
}
