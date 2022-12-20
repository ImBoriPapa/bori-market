package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeType;
import com.bmarket.tradeservice.dto.RequestForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
@Transactional
class TradeCommandServiceTest {

    @Autowired
    TradeCommandService tradeCommandService;

    @Test
    @DisplayName("createTrade 테스트")
    void createTradeTest() throws Exception{
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(UUID.randomUUID().toString())
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("images", "image4.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("images", "image5.png", "image/png", "imageContent".getBytes());
        List<MultipartFile> images = List.of(image1, image2, image3, image4, image5);
        //when
        tradeCommandService.createTrade(form,images);

        //then

    }

}