package com.bmarket.tradeservice.api.responseForm;

import com.bmarket.tradeservice.exception.custom_exception.BasicException;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
public class ResponseForm<T> {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Of<T> {
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
    @AllArgsConstructor
    @Getter
    public static class Error {
        private ResponseStatus status;
        private String requestPath;
        private int errorCode;
        private List<String> message;

        public Error(Exception ex, String request) {
            this.status = ResponseStatus.ERROR;
            this.requestPath = request;
            this.errorCode = ResponseStatus.ERROR.getCode();
            this.message = List.of(ResponseStatus.ERROR.getMessage());
        }

        public Error(BasicException ex, String request) {
            this.status = ex.getStatus();
            this.requestPath = request;
            this.errorCode = ex.getErrorCode();
            this.message = ex.getErrorMessage();
        }
    }
}
