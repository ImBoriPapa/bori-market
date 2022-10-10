package com.bmarket.securityservice.exception.custom_exception;

import com.bmarket.securityservice.exception.error_code.ErrorCode;
import org.springframework.context.MessageSourceResolvable;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class BasicException extends RuntimeException{

    private String errorType;
    int errorCode;
    private List<String> errorMessage;

    public String getErrorType(){
        return errorType;}

    public int getErrorCode(){
        return errorCode;
    }

    public List<String> getErrorMessage(){
        return errorMessage;
    }

    public BasicException(ErrorCode code ) {
        this.errorType = this.getClass().getName();
        this.errorCode = code.getErrorCode();
        this.errorMessage = List.of(code.getErrorMessage());
    }

    public BasicException(ErrorCode code ,BindingResult bindingResult) {
        this.errorType = this.getClass().getName();
        this.errorCode = code.getErrorCode();
        this.errorMessage = getErrorList(bindingResult);

    }

    private static List<String> getErrorList(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    }
}
