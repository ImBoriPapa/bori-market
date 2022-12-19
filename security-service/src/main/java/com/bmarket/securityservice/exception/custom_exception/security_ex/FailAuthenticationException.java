package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class FailAuthenticationException extends BasicException {
    public FailAuthenticationException() {
    }

    public FailAuthenticationException(ResponseStatus status) {
        super(status);
    }

    public FailAuthenticationException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
