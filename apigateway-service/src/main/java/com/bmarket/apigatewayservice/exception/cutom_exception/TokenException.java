package com.bmarket.apigatewayservice.exception.cutom_exception;

import lombok.Getter;

@Getter
public class TokenException extends BasicException{

    public TokenException() {
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(ResponseStatus status) {
        super(status);
    }

}
