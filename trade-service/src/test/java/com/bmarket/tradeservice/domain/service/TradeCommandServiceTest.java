package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.dto.RequestUpdateForm;
import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.assertj.core.api.Assertions;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class TradeCommandServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    TradeCommandService tradeCommandService;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    ObjectMapper objectMapper;
    private Dispatcher dispatcher;
    private MockWebServer mockWebServer;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String imagePath4;
    private String imagePath5;
    private String imagePath6;
    private final String tradeImageId = UUID.randomUUID().toString();

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/trade");

        imagePath1 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi1.png";
        imagePath2 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi2.png";
        imagePath3 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi3.png";

        imagePath4 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi4.png";
        imagePath5 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi5.png";
        imagePath6 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi6.png";


        List<String> list = List.of(imagePath1, imagePath2, imagePath3);
        List<String> list2 = List.of(imagePath4, imagePath5, imagePath6);

        MockResponse post = new MockResponse();
        post.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        post.setBody(objectMapper
                .writeValueAsString(new ResponseImageDto(true, tradeImageId, list)));

        MockResponse delete = new MockResponse();
        delete.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        delete.setBody(objectMapper
                .writeValueAsString(new ResponseImageDto(true, tradeImageId, null)));

        MockResponse put = new MockResponse();
        put.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        put.setBody(objectMapper
                .writeValueAsString(new ResponseImageDto(true, tradeImageId, list2)));

        dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {

                if (request.getMethod().equals("POST")) {
                    return post;
                }
                if (request.getMethod().equals("DELETE")) {
                    return delete;
                }
                if (request.getMethod().equals("PUT")) {
                    return put;
                }


                return new MockResponse().setResponseCode(400);
            }
        };

        mockWebServer.setDispatcher(dispatcher);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
        dispatcher.shutdown();

        tradeImageRepository.deleteAll();
        tradeImageRepository.deleteAll();
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
        assertThat(tradeImages.get(0).getImagePath()).isEqualTo(imagePath1);
        assertThat(tradeImages.get(1).getImagePath()).isEqualTo(imagePath2);
        assertThat(tradeImages.get(2).getImagePath()).isEqualTo(imagePath3);
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() throws Exception {
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
        tradeCommandService.deleteTrade(trade.getId());
        Optional<Trade> tradeOptional = tradeRepository.findById(trade.getId());
        List<TradeImage> tradeImages = tradeImageRepository.findByTrade(trade);
        //then
        assertThat(tradeOptional).isEmpty();
        assertThat(tradeImages).isEmpty();

    }

    @Test
    @DisplayName("수정 테스트")
    void putTest() throws Exception {
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
                .category(Category.ETC)
                .price(1000)
                .address(address)
                .isShare(false)
                .isOffer(false).build();

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());
        List<MultipartFile> multipartFiles = List.of(file1, file2, file3);
        Trade trade = tradeCommandService.createTrade(form, multipartFiles);
        List<MultipartFile> multipartFiles2 = List.of(file1, file2, file3);

        Address newAddress = Address.builder()
                .addressCode(1020)
                .city("인천시")
                .district("동작구")
                .town("갈현동").build();

        RequestUpdateForm updateForm = RequestUpdateForm.builder()
                .title("자동차 팔아요")
                .context("1000만번 탄 자동차")
                .price(100000)
                .category(Category.BOOK)
                .address(newAddress)
                .isShare(true)
                .isOffer(true).build();

        //when
        Trade updateTrade = tradeCommandService.updateTrade(trade.getId(), updateForm, multipartFiles2);

        Trade byId = tradeRepository.findById(updateTrade.getId()).get();
        //then
        assertThat(byId.getTitle()).isEqualTo(updateForm.getTitle());
        assertThat(byId.getContext()).isEqualTo(updateForm.getContext());
        assertThat(byId.getPrice()).isEqualTo(updateForm.getPrice());
        assertThat(byId.getIsShare()).isEqualTo(updateForm.getIsShare());
        assertThat(byId.getIsOffer()).isEqualTo(updateForm.getIsOffer());
        assertThat(byId.getRepresentativeImage()).isNotEqualTo(trade.getRepresentativeImage());
    }

}