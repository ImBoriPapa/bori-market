package com.bmarket.securityservice.internal_api.trade;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseTradeResult;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.internal_api.trade.form.CreateTradeServiceForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
class RequestTradeApiTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RequestTradeApi requestTradeApi;

    private MockWebServer mockWebServer;
    private Dispatcher dispatcher;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);

        ResponseTradeResult created = ResponseTradeResult.builder()
                .success(true)
                .tradeId(1L)
                .createdAt(LocalDateTime.now()).build();

        ResponseTradeResult deleted = ResponseTradeResult.builder()
                .success(true)
                .tradeId(null)
                .createdAt(null).build();


        MockResponse postResponse = new MockResponse();
        postResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        postResponse.setBody(objectMapper.writeValueAsString(created));

        MockResponse putResponse = new MockResponse();
        putResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        putResponse.setBody(objectMapper.writeValueAsString(created));

        MockResponse deleteResponse = new MockResponse();
        deleteResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        deleteResponse.setBody(objectMapper.writeValueAsString(deleted));

        dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {

                if (request.getMethod().equals("POST")) {
                    return postResponse;
                }

                if (request.getMethod().equals("PUT")) {
                    return putResponse;
                }
                if (request.getMethod().equals("DELETE")) {
                    return deleteResponse;
                }

                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
    }

    @AfterEach
    void afterEach() throws IOException {
        dispatcher.shutdown();
        mockWebServer.shutdown();

    }

    @Test
    @DisplayName("trade 생성 테스트")
    void postTradeTest() throws Exception {
        //given
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동").build();
        CreateTradeServiceForm form = CreateTradeServiceForm.builder()
                .accountId(1L)
                .title("제목")
                .context("내용내용내용")
                .price(15000)
                .address(address)
                .category(Category.ETC)
                .isShare(false)
                .isOffer(false).build();
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "dsadsada".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "dsadsada".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "dsadsada".getBytes());
        //when
        ResponseTradeResult responseTradeResult = requestTradeApi.requestCreateTrade(form, List.of(image1, image2, image3));
        //then
        assertThat(responseTradeResult.getSuccess()).isTrue();
        assertThat(responseTradeResult.getTradeId()).isEqualTo(1L);
        assertThat(responseTradeResult.getTradeId()).isNotNull();
    }

    @Test
    @DisplayName("수정 요청 테스트")
    void PutTradeTest() throws Exception {
        //given
        RequestTradeForm.ModifyTradeForm form = RequestTradeForm.ModifyTradeForm.builder()
                .title("제목제목")
                .context("내용내용")
                .price(15000)
                .category(Category.ETC)
                .isShare(true)
                .isOffer(true).build();
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "dsadsada".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "dsadsada".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "dsadsada".getBytes());
        //when
        ResponseTradeResult responseTradeResult = requestTradeApi.requestPutTrade(1L, form, List.of(image1, image2, image3));
        //then
        assertThat(responseTradeResult.getSuccess()).isTrue();
        assertThat(responseTradeResult.getTradeId()).isEqualTo(1L);
        assertThat(responseTradeResult.getTradeId()).isNotNull();
    }


    @Test
    @DisplayName("삭제 요청 테스트")
    void deleteTest() throws Exception {
        //given
        Long tradeId = 1L;
        //when
        ResponseTradeResult responseTradeResult = requestTradeApi.requestDeleteTrade(tradeId);
        //then
        assertThat(responseTradeResult.getSuccess()).isTrue();
        assertThat(responseTradeResult.getTradeId()).isNull();
        assertThat(responseTradeResult.getTradeId()).isNull();

    }

}