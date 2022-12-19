package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class PasswordNotCorrectException extends BasicException {
    public PasswordNotCorrectException() {
    }

    public PasswordNotCorrectException(ResponseStatus status) {
        super(status);
    }

    public PasswordNotCorrectException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
