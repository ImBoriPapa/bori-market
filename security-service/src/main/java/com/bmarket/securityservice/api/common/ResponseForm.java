package com.bmarket.securityservice.api.common;



import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ResponseForm {

    @NoArgsConstructor
    @Getter
    public static class Of<T>{
        private ResponseStatus status;
        private int code;
        private String message;
        private T result;

        public Of(ResponseStatus status, T result) {
            this.status = status;
            this.code = status.getCode();
            this.message = status.getMessage();
            this.result = result;
        }
    }
    @NoArgsConstructor
    @Getter
    public static class Error {
        private ResponseStatus status;
        private String errorType;
        private int errorCode;
        private List<String> message;

        public Error(BasicException ex) {
            this.status = ex.getStatus();
            this.errorType = ex.getErrorType();
            this.errorCode = ex.getErrorCode();
            this.message = ex.getErrorMessage();
        }

    }

}
