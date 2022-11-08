package com.bmarket.securityservice.exception.custom_exception;


import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BasicException extends RuntimeException {

    private  ResponseStatus status;
    private String errorType;
    private int errorCode;
    private List<String> errorMessage;

    public ResponseStatus getStatus() {
        return status;
    }

    public String getErrorType() {
        return errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public BasicException() {
    }

    public BasicException(ResponseStatus status) {
        this.status = status;
        this.errorType = this.getClass().getName();
        this.errorCode = status.getCode();
        this.errorMessage = List.of(status.getMessage());
        log.info("Custom Exception 발생={}", this);
    }

    public BasicException(ResponseStatus status, BindingResult bindingResult) {
        this.status = status;
        this.errorType = this.getClass().getName();
        this.errorCode = status.getCode();
        this.errorMessage = getErrorList(bindingResult);
        log.info("Custom Exception 발생={}", this.getErrorMessage());
    }

    private static List<String> getErrorList(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    }

}
