package com.bmarket.securityservice.api.common;



import com.bmarket.securityservice.utils.status.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseForm <T>{
    private ResponseStatus status;
    private int code;
    private String message;
    private T result;

    public ResponseForm(ResponseStatus status, T result) {
        this.status = status;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }

}
