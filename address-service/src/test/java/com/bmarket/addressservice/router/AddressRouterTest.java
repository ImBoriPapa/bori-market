package com.bmarket.addressservice.router;

import com.bmarket.addressservice.dto.AddressResult;
import com.bmarket.addressservice.handler.AddressHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@Slf4j
class AddressRouterTest {

    @Autowired
    AddressRouter addressRouter;
    @Autowired
    AddressHandler addressHandler;
    @Autowired
    ObjectMapper objectMapper;


    WebTestClient client;

    @BeforeEach
    void beforeEach() {
        client = WebTestClient
                .bindToRouterFunction(addressRouter.search(addressHandler))
                .build();
    }

    @Test
    @DisplayName("search 전체 조회 테스트")
    void searchAllTest() throws Exception {
        //given
        //when
        //then
        client.get()
                .uri("/address")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AddressResult.class)
                .hasSize(425);
    }

    @Test
    @DisplayName("search 동네 조회 테스트")
    void searchTownTest() throws Exception {
        //given
        String town = "삼성동";
        //when
        //then
        client.get()
                .uri("/address?town={town}", town)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].city").isEqualTo("서울시")
                .jsonPath("$[0].district").isEqualTo("관악구")
                .jsonPath("$[0].town").isEqualTo("삼성동")
                .jsonPath("$[0].addressCode").isEqualTo(1089);
    }

    @Test
    @DisplayName("검색 창 테스트")
    void searchFormTest() throws Exception {
        //given
        //when
        //then
        client.get()
                .uri("/address/search-form")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.TEXT_HTML);
    }
}