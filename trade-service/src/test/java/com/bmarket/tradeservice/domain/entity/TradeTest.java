package com.bmarket.tradeservice.domain.entity;

import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.bmarket.tradeservice.domain.entity.Category.*;
import static com.bmarket.tradeservice.domain.entity.TradeStatus.SOLD_OUT;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
@Transactional
class TradeTest {

    @Autowired
    TradeRepository tradeRepository;

    @Test
    @DisplayName("trade 생성 테스트")
    void createTradeTest() throws Exception {
        //given

        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동").build();

        Trade trade = Trade.createTrade()
                .accountId(1L)
                .title("제목1")
                .context("내용입니다. 물건을 팔아보겠습니다.")
                .price(10000)
                .address(address)
                .category(BOOK)
                .isShare(false)
                .isOffer(false)
                .representativeImage("http://localhost:8095/frm/default.png")
                .build();
        //when
        Trade created = tradeRepository.save(trade);
        Trade findTrade = tradeRepository.findById(created.getId())
                .orElseThrow(() -> new IllegalArgumentException("판매글을 찾을수 없습니다."));
        //then
        assertThat(findTrade.getId()).isEqualTo(created.getId());
        assertThat(findTrade.getTitle()).isEqualTo(created.getTitle());
        assertThat(findTrade.getContext()).isEqualTo(created.getContext());
        assertThat(findTrade.getPrice()).isEqualTo(created.getPrice());
        assertThat(findTrade.getAddress()).isEqualTo(created.getAddress());
        assertThat(findTrade.getCategory()).isEqualTo(created.getCategory());
        assertThat(findTrade.getIsShare()).isEqualTo(created.getIsShare());
        assertThat(findTrade.getIsOffer()).isEqualTo(created.getIsOffer());
        assertThat(findTrade.getRepresentativeImage()).isEqualTo(created.getRepresentativeImage());
        assertThat(findTrade.getStatus()).isEqualTo(created.getStatus());
        assertThat(findTrade.getCreatedAt()).isEqualTo(created.getCreatedAt());
        assertThat(findTrade.getUpdatedAt()).isEqualTo(created.getUpdatedAt());
    }
}