package com.bmarket.securityservice.exception.custom_exception.security_ex;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.validation.BindingResult;

import java.util.List;

public class NotFoundAccountException extends BasicException {
    @Override
    public String getErrorType() {
        return super.getErrorType();
    }

    @Override
    public int getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public List<String> getErrorMessage() {
        return super.getErrorMessage();
    }

    public NotFoundAccountException(ErrorCode code) {
        super(code);
    }

    public NotFoundAccountException(ErrorCode code, BindingResult bindingResult) {
        super(code, bindingResult);
    }
}
