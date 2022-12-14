package com.bmarket.tradeservice.domain.repository.query;


import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;
import com.bmarket.tradeservice.domain.sample.SampleProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
@Transactional
class TradeQueryRepositoryImplTest {

    @Autowired
    TradeQueryRepositoryImpl tradeQueryRepository;
    @Autowired
    SampleProvider provider;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;

    @BeforeEach
    void beforeEach() {
        provider.initSampleData();
    }

    @AfterEach
    void afterEach() {
        provider.deleteSampleData();
    }

    /**
     *  Sample data  총 100개
     *  AddressCode 1001 60 개
     *  -Category = BOOK 20 개 ,isShare= false ,isOffer= false
     *  -Category = PAT 20 개  ,isShare= false ,isOffer= false
     *  -Category = FOOD 20 개 ,isShare= true ,isOffer=  false
     *
     *  AddressCode 1002 20 개
     *  -Category = BABY_CLOTH ,isShare= false ,isOffer= true
     *  AddressCode 1003 20 개
     *  -Category = GAME       ,isShare= false ,isOffer= true
     */

    /**
     * 시나리오 1
     * 다음 페이지 true 를 확인 할수 있는가 ,다음 페이지 false 를 확인할 수 있는가
     * SampleData 총 100
     * addressCode 1001 = 60개, addressCode 1002 = 20개 ,addressCode 1002 = 20개
     * 검색 방향 DESC
     */
    @Test
    @DisplayName("검색 조건 addressCode = 1001 ")
    void searchList1() throws Exception {
        //given
        SearchCondition condition = SearchCondition.builder()
                .addressCode(1001)
                .range(AddressRange.ONLY)
                .build();
        //when

        TradeListDto lastIndex0 = tradeQueryRepository
                .findTradeListWithCondition(20, 0L, condition);

        TradeListDto lastIndex41 = tradeQueryRepository
                .findTradeListWithCondition(20, 21L, condition);
        //then

        assertThat(lastIndex0.getSize()).isEqualTo(21);
        assertThat(lastIndex0.getHasNext()).isEqualTo(true);

        assertThat(lastIndex41.getSize()).isEqualTo(20);
        assertThat(lastIndex41.getHasNext()).isEqualTo(false);
    }

    /**
     * 시나리오 2
     * addressCode 1001 = 60개중 Category PAT 검색 예상 20개
     * 검색 방향 DESC
     */
    @Test
    @DisplayName("검색 조건 addressCode = 1001 category = PAT")
    void searchList2() throws Exception {
        //given
        SearchCondition condition = SearchCondition.builder()
                .addressCode(1001)
                .category(Category.PAT)
                .range(AddressRange.ONLY)
                .build();
        //when
        TradeListDto lastIndex0 = tradeQueryRepository
                .findTradeListWithCondition(20, 0L, condition);
        //then
        assertThat(lastIndex0.getSize()).isEqualTo(20);
        assertThat(lastIndex0.getHasNext()).isEqualTo(false);
    }

    /**
     * 시나리오 3
     * addressCode 1001 = 60개중 isShare = true 검색 예상 20개
     * 검색 방향 DESC
     */
    @Test
    @DisplayName("검색 조건 addressCode = 1001 isShare = true")
    void searchList3() throws Exception {
        //given
        SearchCondition condition = SearchCondition.builder()
                .addressCode(1001)
                .isShare(true)
                .range(AddressRange.ONLY)
                .build();
        //when
        TradeListDto lastIndex0 = tradeQueryRepository
                .findTradeListWithCondition(20, 0L, condition);
        //then
        assertThat(lastIndex0.getSize()).isEqualTo(20);
        assertThat(lastIndex0.getHasNext()).isEqualTo(false);
    }

    /**
     * 시나리오 4
     * addressCode = 1001, range = five 은 총 100개에서 Category = BABY_CLOTH 는 20개이고 동네이름은 = 관악동
     * 검색 방향 DESC
     */
    @Test
    @DisplayName("검색 조건 addressCode = 1001, range = five,Category = BABY_CLOTH ")
    void searchList4() throws Exception {
        //given
        SearchCondition condition = SearchCondition.builder()
                .addressCode(1001)
                .category(Category.BABY_CLOTH)
                .range(AddressRange.FIVE)
                .build();
        //when
        TradeListDto lastIndex0 = tradeQueryRepository
                .findTradeListWithCondition(20, 0L, condition);
        //then
        assertThat(lastIndex0.getSize()).isEqualTo(20);
        assertThat(lastIndex0.getHasNext()).isEqualTo(false);
        assertThat(lastIndex0.getResult().get(0).getTownName()).isEqualTo("관악동");
    }

    /**
     * 상세 조회 테스트
     */
    @Test
    @DisplayName("판매글 상세 조회 테스트")
    void findTradeDetailTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(3000)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        Trade trade = Trade.createTrade()
                .title("제목입니다.")
                .context("내용입니다")
                .address(address)
                .category(Category.ETC)
                .price(1000000)
                .isShare(false)
                .isOffer(false)
                .build();
        Trade saveTrade = tradeRepository.save(trade);

        TradeImage.createImage()
                .trade(saveTrade)
                .imageId(UUID.randomUUID().toString())
                .imagePath("http://localhost:8095//" + UUID.randomUUID() + ".PNG");
        //when

        TradeDetailDto findDetail = tradeQueryRepository.findTradeDetailById(saveTrade.getId())
                .orElseThrow(()->new IllegalArgumentException("판매글 정보가 없습니다."));

        //then
        assertThat(findDetail.getTradeId()).isEqualTo(findDetail.getTradeId());
        assertThat(findDetail.getAccountId()).isEqualTo(findDetail.getAccountId());

    }


}