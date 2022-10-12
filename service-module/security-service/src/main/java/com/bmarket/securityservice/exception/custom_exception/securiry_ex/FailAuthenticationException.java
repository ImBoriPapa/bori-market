package com.bmarket.securityservice.exception.custom_exception.securiry_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;

public class FailAuthenticationException extends BasicException {

    public FailAuthenticationException(ErrorCode code) {
        super(code);
    }

    public FailAuthenticationException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}
