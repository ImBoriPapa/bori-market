package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import org.springframework.validation.BindingResult;

public class InvalidTokenException extends BasicException {
    public InvalidTokenException() {
    }

    public InvalidTokenException(ResponseStatus status) {
        super(status);
    }

    public InvalidTokenException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
