package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class TokenException extends BasicException {
    public TokenException() {
    }

    public TokenException(ResponseStatus status) {
        super(status);
    }

    public TokenException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
