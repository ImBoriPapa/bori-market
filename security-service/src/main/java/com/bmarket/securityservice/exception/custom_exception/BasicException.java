package com.bmarket.securityservice.exception.custom_exception;


import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BasicException extends RuntimeException {

    public BasicException(String message) {
        super(message);
    }

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
        super(status.getMessage());
        this.status = status;
        this.errorType = this.getClass().getName();
        this.errorCode = status.getCode();
        this.errorMessage = List.of(status.getMessage());
        log.info("Custom Exception 발생={}", this.getErrorMessage().stream().findFirst());
    }

    public BasicException(ResponseStatus status, BindingResult bindingResult) {
        super(status.getMessage());
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
