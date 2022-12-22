package com.bmarket.tradeservice.exception.custom_exception;

import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class FileUploadException extends BasicException {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException() {
    }

    public FileUploadException(ResponseStatus status) {
        super(status);
    }

    public FileUploadException(ResponseStatus status, BindingResult bindingResult) {
        super(status, bindingResult);
    }
}
