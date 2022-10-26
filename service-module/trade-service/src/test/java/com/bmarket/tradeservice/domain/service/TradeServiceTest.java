package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.TradeDto;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class TradeServiceTest {

    @Autowired
    TradeService tradeService;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;

    @Test
    @DisplayName("trade 생성 성공 테스트")
    void successCreate() throws Exception{

        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "safsafsa".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "safsafsa".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "safsafsa".getBytes());
        List<MultipartFile> files = List.of(file1, file2, file3);
        //given
        TradeDto dto = TradeDto.builder()
                .accountId(1L)
                .nickname("닉네임")
                .profileImage("profile.jpg")
                .title("타이틀")
                .context("내용")
                .price(150000)
                .addressCode(1001)
                .townName("신월동")
                .category(Category.BEAUTY)
                .isShare(true)
                .isOffer(true)
                .images(files).build();
        Trade trade = tradeService.createTrade(dto);
        //when
        Trade findTrade = tradeRepository.findById(trade.getId()).get();
        List<TradeImage> imageList = tradeImageRepository.findByTrade(trade);
        //then
        assertThat(findTrade.getAccountId()).isEqualTo(1L);
        assertThat(findTrade.getRepresentativeImage()).isEqualTo("test1.jpg");
    }

}