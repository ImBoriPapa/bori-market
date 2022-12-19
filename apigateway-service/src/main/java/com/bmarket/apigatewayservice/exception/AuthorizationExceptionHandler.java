package com.bmarket.apigatewayservice.exception;

import com.bmarket.apigatewayservice.exception.cutom_exception.ResponseStatus;
import com.bmarket.apigatewayservice.exception.cutom_exception.TokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
@Slf4j
@RequiredArgsConstructor
public class AuthorizationExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {


        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof TokenException) {
            return write(exchange.getResponse(), new ErrorResponse(((TokenException) ex).getStatus()));
        }

        return write(exchange.getResponse(), new ErrorResponse(ResponseStatus.NOT_FOUND_REASON));
    }

    public <T> Mono<Void> write(ServerHttpResponse httpResponse, T object) {
        return httpResponse
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = httpResponse.bufferFactory();
                    try {
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(object));
                    } catch (Exception ex) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ErrorResponse {
        private String status;
        private String type;
        private int code;
        private String message;

        public ErrorResponse(ResponseStatus status) {
            this.status = status.name();
            this.type = status.getClass().getName();
            this.code = status.getCode();
            this.message = status.getMessage();
        }
    }
}
