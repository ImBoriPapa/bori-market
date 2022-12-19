package com.bmarket.apigatewayservice.exception.cutom_exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BasicException extends RuntimeException {

    private ResponseStatus status;

    public ResponseStatus getStatus() {
        return status;
    }

    public BasicException() {
    }

    public BasicException(String message) {
        super(message);
    }

    public BasicException(ResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
