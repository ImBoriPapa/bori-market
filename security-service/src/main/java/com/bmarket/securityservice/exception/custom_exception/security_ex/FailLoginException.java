package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import org.springframework.validation.BindingResult;

public class FailLoginException extends BasicException {
    public FailLoginException() {
    }

    public FailLoginException(ResponseStatus status) {
        super(status);
    }

    public FailLoginException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
