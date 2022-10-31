package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;

public class PasswordNotCorrectException extends BasicException {

    public PasswordNotCorrectException(ErrorCode code) {
        super(code);
    }

    public PasswordNotCorrectException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}
