package com.bmarket.tradeservice.domain.entity;

import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Slf4j
@DataJpaTest
@Transactional
class TradeImageTest {

    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    TradeRepository tradeRepository;

    @BeforeEach
    void beforeEach() {
        log.info("[TEST DATA INIT]");
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();

        Trade trade = Trade.createTrade()
                .memberId("member1")
                .title("판매글1")
                .price(10000)
                .context("판매 글 입니다")
                .isOffer(false)
                .representativeImage("http://127.0.0.1/image.png")
                .category(Category.BOOK)
                .tradeType(TradeType.USED_GOODS)
                .address(address)
                .build();
        tradeRepository.save(trade);
        log.info("[TEST DATA INIT FINISH]");
    }

    @AfterEach
    void afterEach() {
        log.info("[TEST DATA DELETE]");
        tradeRepository.deleteAll();
    }

    @Test
    @DisplayName("이미지 단건 생성 테스트")
    void createImageTest1() throws Exception {
        //given
        Trade trade = tradeRepository.findByMemberId("member1")
                .orElseThrow(() -> new IllegalArgumentException("판매 글이 없습니다."));
        TradeImage tradeImage = TradeImage.createImage()
                .originalFileName("original")
                .storedFileName("stored")
                .fullPath("http://aws-s3/stored")
                .size(5000L)
                .fileType("image/png")
                .trade(trade).build();
        //when
        TradeImage save = tradeImageRepository.save(tradeImage);
        List<TradeImage> findImages = tradeImageRepository.findAllByTrade(trade);
        //then
        Assertions.assertThat(findImages.contains(tradeImage)).isTrue();
    }

    @Test
    @DisplayName("이미지 생성 다건 테스트")
    void createImageTest2() throws Exception {
        //given
        Trade trade = tradeRepository.findByMemberId("member1")
                .orElseThrow(() -> new IllegalArgumentException("판매 글이 없습니다."));

        ArrayList<TradeImage> list = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            TradeImage tradeImage = TradeImage.createImage()
                    .originalFileName("original" + i)
                    .storedFileName("stored" + i)
                    .fullPath("http://aws-s3/stored" + i)
                    .size(5000L)
                    .fileType("image/png")
                    .trade(trade).build();
            list.add(tradeImage);
        }
        //when
        List<TradeImage> saveAll = tradeImageRepository.saveAll(list);

        List<TradeImage> findImages = tradeImageRepository.findAllByTrade(trade);
        //then
        Assertions.assertThat(findImages.contains(list.get(0))).isTrue();
        Assertions.assertThat(findImages.contains(list.get(1))).isTrue();
        Assertions.assertThat(findImages.contains(list.get(2))).isTrue();
        Assertions.assertThat(findImages.contains(list.get(3))).isTrue();
        Assertions.assertThat(findImages.contains(list.get(4))).isTrue();
    }


}