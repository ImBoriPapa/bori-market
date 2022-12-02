package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeListDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("local")
@Slf4j
@Transactional
class TradeQueryRepositoryImplTest {

    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    TradeQueryRepositoryImpl tradeQueryRepository;

    @BeforeEach
    void beforeEach(){
        for(int i=1; i<=20; i++){
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("종로구")
                    .town("동작동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목"+i)
                    .context("내용"+i)
                    .price(10000)
                    .category(Category.BOOK)
                    .address(address)
                    .isShare(false)
                    .isOffer(false)
                    .representativeImage("대표이미지 경로1")
                    .build();
            Trade save = tradeRepository.save(trade);
            for(int j=1; j<=5; j++){
                String imagePath = "http://localhost:8095/frm/file/"+UUID.randomUUID() + "." + "png";
                TradeImage tradeImage = TradeImage.createImage()
                        .imageId(UUID.randomUUID().toString())
                        .trade(save)
                        .imagePath(imagePath)
                        .build();
                tradeImageRepository.save(tradeImage);
            }
        }
    }


    @Test
    @DisplayName("검색 조건 없이 리스트 조화")
    void searchListWithoutCondition() throws Exception{
        //given
        SearchCondition condition = SearchCondition.builder()
                .addressCode(1001)
                .range(AddressRange.ONLY)
                .build();
        //when

        ResponseResult<List<TradeListDto>> result = tradeQueryRepository.getTradeWithComplexCondition(20, 0L, condition);
        //then
        log.info("[Size]= {}",result.getSize());
        log.info("[Has Next]= {}",result.getHasNext());
        result.getResult().forEach( r->log.info("[title]= {}",r.getTitle()));

    }

}