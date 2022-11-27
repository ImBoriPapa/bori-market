package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import org.springframework.validation.BindingResult;


public class NotFoundAccountException extends BasicException {
    public NotFoundAccountException() {
    }

    public NotFoundAccountException(ResponseStatus status) {
        super(status);
    }

    public NotFoundAccountException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
