package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.trade.dto.RequestForm;
import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.entity.TradeImage;
import com.bmarket.tradeservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.trade.repository.TradeRepository;
import com.bmarket.tradeservice.domain.trade.service.TradeCommandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class TradeCommandServiceTest {

    @Autowired
    TradeCommandService tradeService;
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
        RequestForm form = RequestForm.builder()
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
                .isOffer(true).build();
        Trade trade = tradeService.createTrade(form,files);
        //when
        Trade findTrade = tradeRepository.findById(trade.getId()).get();
        List<TradeImage> imageList = tradeImageRepository.findByTrade(trade);
        //then
        assertThat(findTrade.getAccountId()).isEqualTo(1L);
        imageList.stream().forEach(m->log.info("imageName={}",m.getImageName()));
    }

}