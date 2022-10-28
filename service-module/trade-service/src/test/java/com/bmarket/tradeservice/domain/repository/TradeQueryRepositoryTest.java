package com.bmarket.tradeservice.domain.repository;

import com.bmarket.tradeservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.trade.repository.TradeRepository;
import com.bmarket.tradeservice.domain.trade.repository.query.*;
import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class TradeQueryRepositoryTest {

    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    TradeQueryRepositoryImpl queryRepository;

    @Autowired
    JPAQueryFactory queryFactory;

    @BeforeEach
    void beforeEach() {
        List<Trade> trades = new ArrayList<>();
        /**
         *  CASE 1:
         *  CATEGORY= BOOK
         *  Is Share= false
         *  Is offer= false
         *  AddressCode = 1001
         *  20 건
          */
        for (int i = 1; i <= 20; i++) {
            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .profileImage("profile.jpg")
                    .nickname("nickname" + i)
                    .townName("동네" + i)
                    .addressCode(1001)
                    .price(100000)
                    .title("제목" + i)
                    .category(Category.BOOK)
                    .context("내용이 있다")
                    .isOffer(false)
                    .isShare(false)
                    .representativeImage("대표이미지.jpg").build();
            trades.add(trade);
        }
        /**
         *  CASE 2:
         *  CATEGORY= DIGITAL
         *  Is Share= false
         *  Is offer= false
         *  AddressCode = 1001
         *  20 건
         */
        for (int i = 21; i <= 40; i++) {

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .profileImage("profile.jpg")
                    .nickname("nickname" + i)
                    .townName("동네" + i)
                    .addressCode(1001)
                    .price(100000)
                    .title("제목" + i)
                    .category(Category.DIGITAL)
                    .context("내용이 있다")
                    .isOffer(false)
                    .isShare(false)
                    .representativeImage("대표이미지.jpg").build();
            trades.add(trade);
        }
        /**
         *  CASE 3:
         *  CATEGORY= ETC
         *  Is Share= true
         *  Is offer= false
         *  AddressCode = 1002
         *  20 건
         */
        for (int i = 41; i <= 60; i++) {
            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .profileImage("profile.jpg")
                    .nickname("nickname" + i)
                    .townName("동네" + i)
                    .addressCode(1002)
                    .price(100000)
                    .title("제목" + i)
                    .category(Category.ETC)
                    .context("내용이 있다")
                    .isOffer(true)
                    .isShare(true)
                    .representativeImage("대표이미지.jpg").build();
            trades.add(trade);
        }
        /**
         *  CASE 4:
         *  CATEGORY= BEAUTY
         *  Is Share= false
         *  Is offer= false
         *  AddressCode = 1010
         *  20 건
         */
        for (int i = 61; i <= 80; i++) {
            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .profileImage("profile.jpg")
                    .nickname("nickname" + i)
                    .townName("동네" + i)
                    .addressCode(1010)
                    .price(100000)
                    .title("제목" + i)
                    .category(Category.BEAUTY)
                    .context("내용이 있다")
                    .isOffer(false)
                    .isShare(false)
                    .representativeImage("대표이미지.jpg").build();
            trades.add(trade);
        }

        /**
         *  CASE 5:
         *  CATEGORY= GAME
         *  Is Share= false
         *  Is offer= true
         *  AddressCode = 1005
         *  20 건
         */
        for (int i = 81; i <= 100; i++) {
            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .profileImage("profile.jpg")
                    .nickname("nickname" + i)
                    .townName("동네" + i)
                    .addressCode(1005)
                    .price(100000)
                    .title("제목" + i)
                    .category(Category.GAME)
                    .context("내용이 있다")
                    .isOffer(true)
                    .isShare(false)
                    .representativeImage("대표이미지.jpg").build();
            trades.add(trade);
        }
        tradeRepository.saveAll(trades);

    }

    @Test
    @DisplayName("거래 조회 테스트1 AddressCode= 1001 , 40건")
    void searchTest() throws Exception {
        //페이징
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        SearchCondition condition = SearchCondition.builder()
                .category(null)
                .isShare(null)
                .isOffer(null)
                .status(null)
                .addressCode(1001)
                .range(AddressRange.JUST).build();
        int size = 10;
        Long tradId = 31L;

        ResponseResult<List<TradeListDto>> result = queryRepository.getTradeWithComplexCondition(size,tradId ,condition);
//        assertThat(result.getSize()).isEqualTo(40);

        log.info("size ={}",result.getSize());
        log.info("HasNext ={}",result.getHasNext());
        result.getResult()
                .forEach(m->log.info("id= {}, title= {}",m.getId(),m.getTitle()));
    }

}