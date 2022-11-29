package com.bmarket.frmservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseError {
    private Boolean success;
    private String message;

    public ResponseError(Boolean success, RuntimeException ex) {
        this.success = success;
        this.message = ex.getMessage();
    }
}
