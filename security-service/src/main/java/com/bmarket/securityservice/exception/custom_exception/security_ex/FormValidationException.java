package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class FormValidationException extends BasicException {

    public FormValidationException() {
    }
    public FormValidationException(ResponseStatus status) {
        super(status);
    }

    public FormValidationException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
