package com.bmarket.securityservice.api.common;



import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @NoArgsConstructor
    @Getter
    public static class Response<T>{
        private ResponseStatus status;
        private int code;
        private String message;
        private T result;

        public Response(ResponseStatus status, T result) {
            this.status = status;
            this.code = status.getCode();
            this.message = status.getMessage();
            this.result = result;
        }
    }
    @NoArgsConstructor
    @Getter
    public static class ErrorResponse{
        private ResponseStatus status;
        private String errorType;
        private int errorCode;
        private List<String> message;

        public ErrorResponse(BasicException ex) {
            this.status = ResponseStatus.ERROR;
            this.errorType = ex.getErrorType();
            this.errorCode = ex.getErrorCode();
            this.message = ex.getErrorMessageList();
        }

    }

}
