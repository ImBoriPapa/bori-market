package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class DeniedTokenException extends BasicException {
    public DeniedTokenException() {
    }

    public DeniedTokenException(ResponseStatus status) {
        super(status);
    }

    public DeniedTokenException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
