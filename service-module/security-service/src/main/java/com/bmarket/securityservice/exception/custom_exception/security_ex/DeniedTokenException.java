package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;

public class DeniedTokenException extends BasicException {

    public DeniedTokenException(ErrorCode code) {
        super(code);
    }

    public DeniedTokenException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}
