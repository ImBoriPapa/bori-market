package com.bmarket.securityservice.api.address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    /**
     * 단순 주소 검색 요청 컨트롤러
     * @param town
     * @return
     */
    @GetMapping("/api/address")
    public Mono<List<AddressResult>> searchAddress(@RequestParam(defaultValue = "ready") String town) {

        return WebClient.create()
                .get()
                .uri("http://localhost:8085/addressData/address?town="+town)
                .retrieve()
                .bodyToFlux(AddressResult.class).collectList();

    }
}
