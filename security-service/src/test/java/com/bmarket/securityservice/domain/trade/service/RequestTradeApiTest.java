package com.bmarket.securityservice.domain.trade.service;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.domain.trade.service.form.RequestTradeForm;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.bmarket.securityservice.domain.trade.service.RequestTradeApi.TRADE_SERVICE_URL;
import static com.bmarket.securityservice.utils.status.ResponseStatus.TRADE_WRONG_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class RequestTradeApiTest {

    @Autowired RequestTradeApi requestTradeApi;

    @Autowired
    ObjectMapper objectMapper;

    MockWebServer mockWebServer;

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("거래 생성 요청 테스트")
    void RequestCreateTradeTest() throws Exception{
        //given
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
        mockWebServer.url("/internal/trade");

        MockResponse mockResponse = new MockResponse();

        ResponseCreateTradeResult result = new ResponseCreateTradeResult(1L, LocalDateTime.now());
        String mockResult = objectMapper.writeValueAsString(result);
        mockResponse.setBody(mockResult);
        mockResponse.setHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        mockWebServer.enqueue(mockResponse);

        MockMultipartFile file1 = new MockMultipartFile("images", "test1.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "test2.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "test3.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "test4.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "test5.jpg", "jpg", "test1".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
        Long accountId = 1L;
        RequestTradeForm form = RequestTradeForm.builder()
                .nickname("trader")
                .title("맥북프로")
                .context("사용한지 1년 안된 맥북프로 네고 사절")
                .category(Category.DIGITAL)
                .price(1000000)
                .isShare(false)
                .isOffer(false).build();
        //when
        ResponseCreateTradeResult createTradeResult = requestTradeApi.requestCreateTrade(accountId, form, files);

        //then
        log.info("result id ={}",createTradeResult.getTradeId());
        log.info("result at={}",createTradeResult.getCreatedAt());
    }

    @Test
    @DisplayName("거래 생성 요청 실패 테스트")
    void requestCreateTradeFailTest() throws Exception{
        //given
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
        mockWebServer.url("/internal/trade");

        MockResponse mockResponse = new MockResponse();

        String message = "잘못된 요청입니다.";
        String mockResult = objectMapper.writeValueAsString(message);
        mockResponse.setBody(mockResult);
        mockResponse.setHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setResponseCode(400);
        mockWebServer.enqueue(mockResponse);

        //when
        MockMultipartFile file1 = new MockMultipartFile("images", "test1.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "test2.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "test3.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "test4.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "test5.jpg", "jpg", "test1".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
        Long accountId = 1L;
        RequestTradeForm form = RequestTradeForm.builder()
                .nickname("trader")
                .title("맥북프로")
                .context("사용한지 1년 안된 맥북프로 네고 사절")
                .category(Category.DIGITAL)
                .price(1000000)
                .isShare(false)
                .isOffer(false).build();

        //then
        Assertions.assertThatThrownBy(() -> requestTradeApi.requestCreateTrade(accountId, form, files))
                .isInstanceOf(InternalRequestFailException.class)
                .hasFieldOrProperty("errorMessage");
    }

    // TODO: 2022/11/16 테스트 보강
    @Test
    @DisplayName("거래내역 조회 요청 테스트")
    void requestGetSaleHistoryTest() throws Exception{
        //given
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
        mockWebServer.url("/internal/trade/1/sale-history");

        MockResponse mockResponse = new MockResponse();

        RequestGetTradeListResult result = RequestGetTradeListResult.builder()
                .tradeId(1L)
                .representativeImage("대표이미지")
                .townName("동네1")
                .title("제목1")
                .price(1000)
                .createdAt(LocalDateTime.now())
                .build();

        String mockResult = objectMapper.writeValueAsString(List.of(result));
        mockResponse.setBody(mockResult);
        mockResponse.setHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        mockWebServer.enqueue(mockResponse);

        //when
        List<RequestGetTradeListResult> results = requestTradeApi.requestGetSaleHistory(1L);
        //then
        for (RequestGetTradeListResult listResult : results) {
            log.info("result={}",listResult.getTitle());
        }

    }
}