package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;


public class EmptyTokenException extends BasicException {
    public EmptyTokenException() {
    }

    public EmptyTokenException(ResponseStatus status) {
        super(status);
    }

    public EmptyTokenException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
