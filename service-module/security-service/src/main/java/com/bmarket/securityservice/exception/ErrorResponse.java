package com.bmarket.securityservice.exception;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import lombok.Getter;

import java.util.List;
@Getter
public class ErrorResponse {

    private String errorType;
    private int code;
    private List<String> errorMessage;

    public ErrorResponse(BasicException ex) {
        this.errorType = ex.getErrorType();
        this.code = ex.getErrorCode();
        this.errorMessage = ex.getErrorMessage();
    }
}
