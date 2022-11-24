package com.bmarket.securityservice.internal_api.address;

import com.bmarket.securityservice.domain.address.AddressResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class RequestAddressApi {

    @Value("${internal.address}")
    public String addressServiceUrl;

    public List<AddressResult> searchByTown(String town){
        return WebClient.create(addressServiceUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/address")
                        .queryParam("town", town)
                        .build()
                )
                .retrieve()
                .bodyToFlux(AddressResult.class).collectList()
                .block();

    }

}
