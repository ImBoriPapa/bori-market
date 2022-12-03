package com.bmarket.tradeservice.domain.sample;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SampleProvider {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;

    /**
     * Sample data  총 100개
     * AddressCode 1001 60 개
     * -Category = BOOK 20 개 ,isShare= false ,isOffer= false
     * -Category = PAT 20 개  ,isShare= false ,isOffer= false
     * -Category = FOOD 20 개 ,isShare= true ,isOffer=  false
     * <p>
     * AddressCode 1002 20 개
     * -Category = BABY_CLOTH ,isShare= false ,isOffer= true
     * AddressCode 1003 20 개
     * -Category = GAME       ,isShare= false ,isOffer= true
     */

    public void initSampleData() {
        log.info("[INIT SAMPLE DATA]");
        sampleData1();
        sampleData2();
        sampleData3();
        sampleData4();
        sampleData5();
        log.info("[INIT SAMPLE DATA FINISH]");
    }

    ;

    public void deleteSampleData() {
        log.info("[DELETE SAMPLE DATA]");
        tradeRepository.deleteAll();
        tradeImageRepository.deleteAll();
    }

    private void sampleData1() {
        for (int i = 1; i <= 20; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("종로구")
                    .town("동작동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목" + i)
                    .context("내용" + i)
                    .price(10000)
                    .category(Category.BOOK)
                    .address(address)
                    .isShare(false)
                    .isOffer(false)
                    .representativeImage("대표이미지 경로1")
                    .build();
            saveTrade(trade);
        }
    }

    private void sampleData2() {
        for (int i = 21; i <= 40; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("종로구")
                    .town("동작동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목" + i)
                    .context("내용" + i)
                    .price(10000)
                    .category(Category.PAT)
                    .address(address)
                    .isShare(false)
                    .isOffer(false)
                    .representativeImage("대표이미지 경로2")
                    .build();
            saveTrade(trade);
        }
    }


    private void sampleData3() {
        for (int i = 41; i <= 60; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("종로구")
                    .town("동작동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목" + i)
                    .context("내용" + i)
                    .price(10000)
                    .category(Category.FOOD)
                    .address(address)
                    .isShare(true)
                    .isOffer(false)
                    .representativeImage("대표이미지 경로3")
                    .build();
            saveTrade(trade);
        }
    }

    private void sampleData4() {
        for (int i = 61; i <= 80; i++) {
            Address address = Address.builder()
                    .addressCode(1002)
                    .city("서울")
                    .district("관악구")
                    .town("관악동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목" + i)
                    .context("내용" + i)
                    .price(10000)
                    .category(Category.BABY_CLOTH)
                    .address(address)
                    .isShare(false)
                    .isOffer(true)
                    .representativeImage("대표이미지 경로4")
                    .build();
            saveTrade(trade);
        }
    }

    private void sampleData5() {
        for (int i = 81; i <= 100; i++) {
            Address address = Address.builder()
                    .addressCode(1003)
                    .city("서울")
                    .district("관악구")
                    .town("관악동")
                    .build();

            Trade trade = Trade.createTrade()
                    .accountId(Long.valueOf(i))
                    .title("제목" + i)
                    .context("내용" + i)
                    .price(10000)
                    .category(Category.GAME)
                    .address(address)
                    .isShare(false)
                    .isOffer(true)
                    .representativeImage("대표이미지 경로5")
                    .build();
            saveTrade(trade);
        }
    }

    private void saveTrade(Trade trade) {
        Trade save = tradeRepository.save(trade);

        for (int j = 1; j <= 5; j++) {
            String imagePath = "http://localhost:8095/frm/file/" + UUID.randomUUID() + "." + "png";
            TradeImage tradeImage = TradeImage.createImage()
                    .imageId(UUID.randomUUID().toString())
                    .trade(save)
                    .imagePath(imagePath)
                    .build();
            tradeImageRepository.save(tradeImage);
        }
    }
}
