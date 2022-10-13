package com.bmarket.securityservice.exception.custom_exception.securiry_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;

public class ExpiredTokenException extends BasicException {
    public ExpiredTokenException(ErrorCode code) {
        super(code);
    }

    public ExpiredTokenException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}
