package com.bmarket.frmservice.domain.trade.service;

import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class TradeImageServiceTest {

    @Autowired
    TradeImageService tradeImageService;
    @Autowired
    TradeImageRepository tradeImageRepository;

    @Value("${resource-path.trade-image-path}")
    private String path;

    @Test
    @DisplayName("trade 이미지 저장 테스트")
    void successSave() throws Exception{
        //given
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());

        List<MultipartFile> files = List.of(file1, file2, file3);
        //when
        TradeImageService.ResultSave resultSave = tradeImageService.saveImage(1L, files);
        //then
        assertThat(resultSave.getImagePath().get(0)).isNotEqualTo(file1.getOriginalFilename());
        assertThat(resultSave.getImagePath().get(1)).isNotEqualTo(file2.getOriginalFilename());
        assertThat(resultSave.getImagePath().get(2)).isNotEqualTo(file3.getOriginalFilename());



    }

}