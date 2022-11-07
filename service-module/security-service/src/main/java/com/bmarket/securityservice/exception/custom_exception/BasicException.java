package com.bmarket.securityservice.exception.custom_exception;


import com.bmarket.securityservice.utils.status.ResponseStatus;
import org.springframework.context.MessageSourceResolvable;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class BasicException extends RuntimeException {

    private String errorType;
    int errorCode;
    private List<String> errorMessage;

    public String getErrorType() {
        return errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public List<String> getErrorMessageList() {
        return errorMessage;
    }

    public BasicException(ResponseStatus status) {
        this.errorType = this.getClass().getName();
        this.errorCode = status.getCode();
        this.errorMessage = List.of(status.getMessage());
    }

    public BasicException(ResponseStatus status, BindingResult bindingResult) {
        this.errorType = this.getClass().getName();
        this.errorCode = status.getCode();
        this.errorMessage = getErrorList(bindingResult);

    }
    private static List<String> getErrorList(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    }
}
