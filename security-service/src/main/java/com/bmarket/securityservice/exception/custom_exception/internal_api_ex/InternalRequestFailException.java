package com.bmarket.securityservice.exception.custom_exception.internal_api_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class InternalRequestFailException extends BasicException {

    public InternalRequestFailException() {
    }

    public InternalRequestFailException(ResponseStatus status) {
        super(status);
    }

    public InternalRequestFailException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
