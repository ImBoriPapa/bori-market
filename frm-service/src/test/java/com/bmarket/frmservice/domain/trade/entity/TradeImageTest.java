package com.bmarket.frmservice.domain.trade.entity;

import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Slf4j
class TradeImageTest {

    @Autowired
    TradeImageRepository tradeImageRepository;

    @Test
    @DisplayName("거래 이미지 생성 테스트")
    void createTradeImage(){
        UploadFile file1 = new UploadFile("test1.jpg","32131232.jpg");
        UploadFile file2 = new UploadFile("test2.jpg","3213fsa232.jpg");
        UploadFile file3 = new UploadFile("test3.jpg","3213ag32.jpg");

        List<UploadFile> files = List.of(file1, file2, file3);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .tradeId(1L)
                .images(files).build();

        TradeImage save = tradeImageRepository.save(tradeImage);
        TradeImage find = tradeImageRepository.findById(save.getId()).get();


    }

}