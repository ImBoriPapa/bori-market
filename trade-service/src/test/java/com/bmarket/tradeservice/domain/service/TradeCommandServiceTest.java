package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class TradeCommandServiceTest {

    @Autowired
    TradeCommandService tradeCommandService;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    ObjectMapper objectMapper;

    private MockWebServer mockWebServer;

    private String imagePath1;
    private String imagePath2;
    private String imagePath3;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/trade");

        imagePath1 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi1.png";
        imagePath2 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi2.png";
        imagePath3 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi3.png";
        List<String> list = List.of(imagePath1, imagePath2, imagePath3);

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody(objectMapper
                .writeValueAsString(new ResponseImageDto(list)));
        mockWebServer.enqueue(mockResponse);

    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("createTrade 테스트")
    void createTradeTest() throws Exception {
        //given

        Address address = Address.builder()
                .addressCode(1001)
                .city("서울시")
                .district("서울구")
                .town("서울동").build();

        RequestForm form = RequestForm.builder()
                .accountId(1L)
                .title("자전거 팔아요")
                .context("100만번 탄 자전거")
                .price(1000)
                .address(address)
                .isShare(false)
                .isOffer(false).build();

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());
        List<MultipartFile> multipartFiles = List.of(file1, file2, file3);
        //when
        Trade trade = tradeCommandService.createTrade(form, multipartFiles);
        List<TradeImage> tradeImages = tradeImageRepository.findByTrade(trade);
        //then
        Assertions.assertThat(tradeImages.get(0).getImagePath()).isEqualTo(imagePath1);
        Assertions.assertThat(tradeImages.get(1).getImagePath()).isEqualTo(imagePath2);
        Assertions.assertThat(tradeImages.get(2).getImagePath()).isEqualTo(imagePath3);
    }

}