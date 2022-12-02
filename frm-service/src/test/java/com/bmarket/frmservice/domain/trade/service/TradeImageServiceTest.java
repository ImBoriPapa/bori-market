package com.bmarket.frmservice.domain.trade.service;

import com.bmarket.frmservice.domain.trade.dto.ResponseTradeImage;
import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@Slf4j
class TradeImageServiceTest {

    @Autowired
    TradeImageService tradeImageService;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    FileManager fileManager;

    @Value("${resource-path.trade-image-path}")
    private String path;

    @AfterEach
    void afterEach() {
        tradeImageRepository.deleteAll();
        fileManager.deleteAllInPath(path);
    }

    @Test
    @DisplayName("trade 이미지 저장 테스트")
    void successSave() throws Exception {
        //given
        String tradeImageId = UUID.randomUUID().toString();
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());

        List<MultipartFile> files = List.of(file1, file2, file3);
        //when
        ResponseTradeImage tradeImage = tradeImageService.createTradeImage(files);
        TradeImage findTrade = tradeImageRepository.findById(tradeImageId).orElseThrow(() -> new IllegalArgumentException("찾지 못했습니다."));
        //then
        assertThat(tradeImage.getSuccess()).isTrue();
        assertThat(tradeImage.getImageId()).isEqualTo(findTrade.getId());
        assertThat(tradeImage.getImagePath().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    void successDelete() throws Exception {
        //given
        String tradeImageId = UUID.randomUUID().toString();
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());
        List<MultipartFile> files = List.of(file1, file2, file3);

        //when
        List<UploadFile> uploadFiles = fileManager.saveFile(path, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .images(uploadFiles)
                .build();
        TradeImage save = tradeImageRepository.save(tradeImage);

        ResponseTradeImage deleteImages = tradeImageService.deleteImages(tradeImageId);
        //then
        assertThat(deleteImages.getImageId()).isEqualTo(save.getId());
        assertThat(deleteImages.getSuccess()).isTrue();
        assertThat(deleteImages.getImagePath()).isEqualTo(Collections.EMPTY_LIST);

        assertThat(new File(path, save.getImages().get(0).getStoredImageName())).doesNotExist();
        assertThat(new File(path, save.getImages().get(1).getStoredImageName())).doesNotExist();
        assertThat(new File(path, save.getImages().get(2).getStoredImageName())).doesNotExist();
    }

    @Test
    @DisplayName("이미지 수정 테스트")
    void updateTradeImageTest() throws Exception {
        //given
        String tradeImageId = UUID.randomUUID().toString();
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());
        List<MultipartFile> files = List.of(file1, file2, file3);

        List<UploadFile> uploadFiles = fileManager.saveFile(path, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .images(uploadFiles)
                .build();

        TradeImage save = tradeImageRepository.save(tradeImage);
        save.getImages().forEach(m -> log.info("image={}", m.getUploadImageName()));
        //when
        MockMultipartFile newFile1 = new MockMultipartFile("test1", "new1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile newFile2 = new MockMultipartFile("test2", "new2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile newFile3 = new MockMultipartFile("test3", "new3.jpg", "jpg", "fsafaf".getBytes());
        List<MultipartFile> newFiles = List.of(newFile1, newFile2, newFile3);

        ResponseTradeImage responseTradeImage = tradeImageService.updateTradeImage(tradeImageId, newFiles);

        TradeImage updated = tradeImageRepository.findById(tradeImageId)
                .orElseThrow(() -> new IllegalArgumentException("판매 상품 이미지를 찾을 수 없습니다."));

        updated.getImages().forEach(m -> log.info("image={}", m.getUploadImageName()));

        //then
        assertThat(updated.getImages().get(0).getUploadImageName()).isEqualTo(newFile1.getOriginalFilename());
        assertThat(updated.getImages().get(1).getUploadImageName()).isEqualTo(newFile2.getOriginalFilename());
        assertThat(updated.getImages().get(2).getUploadImageName()).isEqualTo(newFile3.getOriginalFilename());

    }

}