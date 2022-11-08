package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import org.springframework.validation.BindingResult;

public class ExpiredTokenException extends BasicException {
    public ExpiredTokenException() {
    }

    public ExpiredTokenException(ResponseStatus status) {
        super(status);
    }

    public ExpiredTokenException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
