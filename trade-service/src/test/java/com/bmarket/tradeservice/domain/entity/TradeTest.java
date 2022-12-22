package com.bmarket.tradeservice.domain.entity;

import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Slf4j
@ActiveProfiles("dev")
class TradeTest {

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
    @DisplayName("판매글 생성 테스트")
    void createTest1() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();

        Trade trade = Trade.createTrade()
                .memberId(UUID.randomUUID().toString())
                .title("판매글1")
                .price(10000)
                .context("판매 글 입니다")
                .isOffer(false)
                .representativeImage("http://127.0.0.1/image.png")
                .category(Category.BOOK)
                .tradeType(TradeType.USED_GOODS)
                .address(address)
                .build();
        Trade save = tradeRepository.save(trade);
        //when
        Trade findTrade = tradeRepository.findById(save.getId()).orElseThrow(() -> new IllegalArgumentException("판매글을 찾응 수 없습니다."));
        //then
        assertThat(findTrade.getId()).isEqualTo(save.getId());
    }

    @Test
    @DisplayName("판매 글 수정 테스트")
    void modifyTest() throws Exception {
        //given
        Trade trade = tradeRepository.findByMemberId("member1")
                .orElseThrow(() -> new IllegalArgumentException("판매 글을 찾을 수 없습니다."));
        //when
        Trade.UpdateForm updateForm = Trade.UpdateForm.builder()
                .title("수정된 제목")
                .context("수정된 내용")
                .price(20000)
                .category(Category.PAT)
                .representativeImage("수정된이미지")
                .tradeType(TradeType.SHARE_GOODS)
                .tradeStatus(TradeStatus.SOLD_OUT)
                .isOffer(true)
                .build();
        trade.updateTrade(updateForm);

        //then
        assertThat(trade.getTitle()).isEqualTo("수정된 제목");
        assertThat(trade.getContext()).isEqualTo("수정된 내용");
        assertThat(trade.getPrice()).isEqualTo(20000);
        assertThat(trade.getCategory()).isEqualTo(Category.PAT);
        assertThat(trade.getRepresentativeImage()).isEqualTo("수정된이미지");
        assertThat(trade.getTradeType()).isEqualTo(TradeType.SHARE_GOODS);
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.SOLD_OUT);
        assertThat(trade.getIsOffer()).isEqualTo(true);
    }
}