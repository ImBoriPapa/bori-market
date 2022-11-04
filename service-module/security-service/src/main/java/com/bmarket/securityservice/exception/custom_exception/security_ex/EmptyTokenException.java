package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;


public class EmptyTokenException extends BasicException {

    public EmptyTokenException(ErrorCode code) {
        super(code);
    }

    public EmptyTokenException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}