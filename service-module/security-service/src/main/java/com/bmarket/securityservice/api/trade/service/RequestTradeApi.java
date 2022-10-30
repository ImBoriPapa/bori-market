package com.bmarket.securityservice.api.trade.service;

import com.bmarket.securityservice.api.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.api.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.api.trade.controller.resultForm.ResponseCreateTradeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
public class RequestTradeApi {

    public ResponseCreateTradeResult RequestCreateTrade(RequestTradeForm form, List<MultipartFile> images) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);
        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }

        return WebClient.create()
                .post()
                .uri("http://localhost:8081/internal/trade")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(IllegalArgumentException::new))
                .bodyToMono(ResponseCreateTradeResult.class)
                .block();
    }

    public RequestGetTradeListResult[] RequestGetSaleHistory(Long accountId) {
        return WebClient.create()
                .get()
                .uri("http://localhost:8081/internal/trade/" + accountId + "/sale-history")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(IllegalArgumentException::new))
                .bodyToMono(RequestGetTradeListResult[].class)
                .block();
    }
}
