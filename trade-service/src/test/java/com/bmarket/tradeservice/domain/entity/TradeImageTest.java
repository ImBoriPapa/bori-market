package com.bmarket.tradeservice.domain.entity;

import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.bmarket.tradeservice.domain.entity.Category.BOOK;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Slf4j
class TradeImageTest {

    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;

    @Test
    @DisplayName("이미지 경로 저장 테스트")
    void saveTradeImageTest() throws Exception{
        //given
        Trade trade = Trade.createTrade()
                .accountId(1L)
                .title("제목1")
                .context("내용입니다. 물건을 팔아보겠습니다.")
                .price(10000)
                .category(BOOK)
                .isShare(false)
                .isOffer(false)
                .representativeImage("http://localhost:8095/frm/default.png")
                .build();

        String imagePath1 = "http://localhost:8095/frm/trade/dsad132fagomasf1.png";
        String imagePath2 = "http://localhost:8095/frm/trade/dsad132fagomasf2.png";
        String imagePath3 = "http://localhost:8095/frm/trade/dsad132fagomasf3.png";
        String imagePath4 = "http://localhost:8095/frm/trade/dsad132fagomasf4.png";
        String imagePath5 = "http://localhost:8095/frm/trade/dsad132fagomasf5.png";
        List<String> imageList = List.of(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5);
        ArrayList<TradeImage> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            TradeImage image = TradeImage.createImage()
                    .imagePath(imageList.get(i))
                    .trade(trade).build();
            list.add(image);
        }
        //when
        Trade createdTrade = tradeRepository.save(trade);
        tradeImageRepository.saveAll(list);
        List<TradeImage> findImagePath = tradeImageRepository.findByTrade(trade);
        //then
        assertThat(findImagePath.get(0).getImagePath()).isEqualTo(imagePath1);
        assertThat(findImagePath.get(1).getImagePath()).isEqualTo(imagePath2);
        assertThat(findImagePath.get(2).getImagePath()).isEqualTo(imagePath3);
        assertThat(findImagePath.get(3).getImagePath()).isEqualTo(imagePath4);
        assertThat(findImagePath.get(4).getImagePath()).isEqualTo(imagePath5);
    }

}