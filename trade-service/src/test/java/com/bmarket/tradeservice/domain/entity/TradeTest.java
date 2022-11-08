package com.bmarket.tradeservice.domain.entity;

import com.bmarket.tradeservice.domain.trade.repository.TradeRepository;
import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class TradeTest {

    @Autowired
    TradeRepository tradeRepository;

    @Test
    @DisplayName("Trade 저장 테스트")
    void saveTrade() throws Exception{
        //given
        Trade trade = Trade.createTrade()
                .accountId(1L)
                .nickname("판매하는사람")
                .profileImage("profileImage.jpg")
                .title("롤렉스 서브마리너")
                .context("가지고 싶다 서브마리너")
                .price(100000000)
                .addressCode(1001)
                .townName("삼성동")
                .category(Category.ETC)
                .isShare(false)
                .isOffer(true)
                .representativeImage("image1.jpg").build();
        Trade save = tradeRepository.save(trade);
        //when
        Trade trade1 = tradeRepository.findById(save.getId()).get();
        //then
        assertThat(trade1.getId()).isEqualTo(save.getId());
        assertThat(trade1.getAccountId()).isEqualTo(trade.getAccountId());
        assertThat(trade1.getNickname()).isEqualTo(trade.getNickname());
        assertThat(trade1.getProfileImage()).isEqualTo(trade.getProfileImage());
        assertThat(trade1.getTitle()).isEqualTo(trade.getTitle());
        assertThat(trade1.getContext()).isEqualTo(trade.getContext());
        assertThat(trade1.getPrice()).isEqualTo(trade.getPrice());
        assertThat(trade1.getAddressCode()).isEqualTo(trade.getAddressCode());
        assertThat(trade1.getTownName()).isEqualTo(trade.getTownName());
        assertThat(trade1.getCategory()).isEqualTo(trade.getCategory());
        assertThat(trade1.getIsShare()).isEqualTo(trade.getIsShare());
        assertThat(trade1.getIsOffer()).isEqualTo(trade.getIsOffer());
        assertThat(trade1.getRepresentativeImage()).isEqualTo(trade.getRepresentativeImage());
    }

}