package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import org.springframework.validation.BindingResult;

public class IsLogoutAccountException extends BasicException {

    public IsLogoutAccountException() {
    }

    public IsLogoutAccountException(ResponseStatus status) {
        super(status);
    }

    public IsLogoutAccountException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
