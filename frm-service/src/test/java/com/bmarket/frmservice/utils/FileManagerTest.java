package com.bmarket.frmservice.utils;

import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
class FileManagerTest {
    // TODO: 2022/11/29 삭제 테스트
    @Autowired
    FileManager fileManager;
    @Value("${resource-path.profile-image-path}")
    public String path;

    @AfterEach
    void afterEach() {
        fileManager.deleteAllInPath(path);
    }

    @Test
    @DisplayName("파일 단건 저장 테스트")
    void saveOne() throws Exception {
        //given
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "image".getBytes());
        //when
        UploadFile uploadFile = fileManager.saveFile(path, image);
        File storedFile = new File(path, uploadFile.getStoredImageName());
        //then
        assertThat(uploadFile.getUploadImageName()).isEqualTo(image.getOriginalFilename());
        assertThat(storedFile.exists()).isTrue();
        log.info("[Stored Name ={}]", uploadFile.getStoredImageName());
    }

    @Test
    @DisplayName("파일 여러건 저장 테스트")
    void saveList() throws Exception {
        //given
        MockMultipartFile image1 = new MockMultipartFile("image", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("image", "image3.png", "image/png", "image".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("image", "image4.png", "image/png", "image".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("image", "image5.png", "image/png", "image".getBytes());
        List<MultipartFile> files = List.of(image1, image2, image3, image4, image5);
        //when
        List<UploadFile> uploadFiles = fileManager.saveFile(path, files);
        File[] listFiles = new File(path).listFiles();
        //then
        assertThat(uploadFiles.get(0).getUploadImageName()).isEqualTo(image1.getOriginalFilename());
        assertThat(uploadFiles.get(1).getUploadImageName()).isEqualTo(image2.getOriginalFilename());
        assertThat(uploadFiles.get(2).getUploadImageName()).isEqualTo(image3.getOriginalFilename());
        assertThat(uploadFiles.get(3).getUploadImageName()).isEqualTo(image4.getOriginalFilename());
        assertThat(uploadFiles.get(4).getUploadImageName()).isEqualTo(image5.getOriginalFilename());

        Arrays.stream(listFiles).map(File::getName).forEach(m -> log.info("[Stored Name={}]", m));
    }
}