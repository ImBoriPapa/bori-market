package com.bmarket.tradeservice;

import com.bmarket.tradeservice.domain.entity.*;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
@Component
@RequiredArgsConstructor
@Transactional
public class TestData {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository imageRepository;

    public void initSample1() {
        ArrayList<Trade> tradeList = new ArrayList<>();
        ArrayList<TradeImage> tradeImageList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("서울구")
                    .town("서울동" + i)
                    .build();
            Trade trade = Trade.createTrade()
                    .memberId("tester" + i)
                    .title("판매글" + i)
                    .price(30000)
                    .context("판매 글 입니다")
                    .isOffer(false)
                    .representativeImage("http://127.0.0.1/image" + i + ".png")
                    .category(Category.BOOK)
                    .tradeType(TradeType.USED_GOODS)
                    .address(address)
                    .build();
            tradeList.add(trade);

            for (int j = 1; j <= 5; j++) {
                TradeImage tradeImage = TradeImage.createImage()
                        .originalFileName("original name")
                        .fullPath("https://aws/trade/image" + i + "png")
                        .storedFileName("stored name")
                        .size(3000L)
                        .fileType("this is type")
                        .trade(trade)
                        .build();
                tradeImageList.add(tradeImage);
            }
        }

        tradeRepository.saveAll(tradeList);
        imageRepository.saveAll(tradeImageList);
    }

    public void initSample2() {
        ArrayList<Trade> tradeList = new ArrayList<>();
        ArrayList<TradeImage> tradeImageList = new ArrayList<>();

        for (int i = 31; i <= 51; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("서울구")
                    .town("서울동" + i)
                    .build();
            Trade trade = Trade.createTrade()
                    .memberId("tester" + i)
                    .title("판매글" + i)
                    .price(30000)
                    .context("판매 글 입니다")
                    .isOffer(false)
                    .representativeImage("http://127.0.0.1/image" + i + ".png")
                    .category(Category.BEAUTY)
                    .tradeType(TradeType.USED_GOODS)
                    .address(address)
                    .build();
            tradeList.add(trade);

            for (int j = 1; j <= 5; j++) {
                TradeImage tradeImage = TradeImage.createImage()
                        .originalFileName("original name")
                        .fullPath("https://aws/trade/image" + i + "png")
                        .storedFileName("stored name")
                        .size(3000L)
                        .fileType("this is type")
                        .trade(trade)
                        .build();
                tradeImageList.add(tradeImage);
            }
        }

        tradeRepository.saveAll(tradeList);
        imageRepository.saveAll(tradeImageList);
    }

    public void initSample3() {
        ArrayList<Trade> tradeList = new ArrayList<>();
        ArrayList<TradeImage> tradeImageList = new ArrayList<>();

        for (int i = 51; i <= 81; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("서울구")
                    .town("서울동" + i)
                    .build();
            Trade trade = Trade.createTrade()
                    .memberId("tester" + i)
                    .title("판매글" + i)
                    .price(30000)
                    .context("판매 글 입니다")
                    .isOffer(false)
                    .representativeImage("http://127.0.0.1/image" + i + ".png")
                    .category(Category.PAT)
                    .tradeType(TradeType.SHARE_GOODS)
                    .address(address)
                    .build();
            tradeList.add(trade);

            for (int j = 1; j <= 5; j++) {
                TradeImage tradeImage = TradeImage.createImage()
                        .originalFileName("original name")
                        .fullPath("https://aws/trade/image" + i + "png")
                        .storedFileName("stored name")
                        .size(3000L)
                        .fileType("this is type")
                        .trade(trade)
                        .build();
                tradeImageList.add(tradeImage);
            }
        }

        tradeRepository.saveAll(tradeList);
        imageRepository.saveAll(tradeImageList);
    }

    public void initSample4() {
        ArrayList<Trade> tradeList = new ArrayList<>();
        ArrayList<TradeImage> tradeImageList = new ArrayList<>();

        for (int i = 81; i <= 100; i++) {
            Address address = Address.builder()
                    .addressCode(1001)
                    .city("서울")
                    .district("서울구")
                    .town("서울동" + i)
                    .build();
            Trade trade = Trade.createTrade()
                    .memberId("tester" + i)
                    .title("판매글" + i)
                    .price(30000)
                    .context("판매 글 입니다")
                    .isOffer(false)
                    .representativeImage("http://127.0.0.1/image" + i + ".png")
                    .category(Category.GAME)
                    .tradeType(TradeType.ADVERTISEMENT)
                    .address(address)
                    .build();
            tradeList.add(trade);

            for (int j = 1; j <= 5; j++) {
                TradeImage tradeImage = TradeImage.createImage()
                        .originalFileName("original name")
                        .fullPath("https://aws/trade/image" + i + "png")
                        .storedFileName("stored name")
                        .size(3000L)
                        .fileType("this is type")
                        .trade(trade)
                        .build();
                tradeImageList.add(tradeImage);
            }
        }

        tradeRepository.saveAll(tradeList);
        imageRepository.saveAll(tradeImageList);
    }
}
