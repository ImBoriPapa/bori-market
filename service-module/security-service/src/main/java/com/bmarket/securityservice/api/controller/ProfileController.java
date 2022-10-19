package com.bmarket.securityservice.api.controller;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private String path = "";

    private final AccountRepository accountRepository;

    @PostMapping(value = "/account/{clientId}/profile/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createProfileImage(@PathVariable String clientId, @RequestPart MultipartFile image) {

        Account account = accountRepository.findByClientId(clientId).get();

        Resource imageResource = image.getResource();

        Mono<String> result = WebClient.create("localhost:8095/frm/profile")
                .put()
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("accountId", account.getId())
                        .with("image", imageResource)
                )
                .retrieve()
                .bodyToMono(String.class);
        String block = result.block();
        log.info("result={}", block);
    }


}
