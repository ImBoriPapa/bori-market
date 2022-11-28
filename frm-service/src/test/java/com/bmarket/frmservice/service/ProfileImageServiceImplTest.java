package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static com.bmarket.frmservice.utils.Patterns.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ProfileImageServiceImplTest {

    @Autowired
    ProfileImageServiceImpl profileImageServiceImpl;
    @Autowired
    ProfileImageRepository profileImageRepository;
    @Autowired
    FileManager generator;

    @Value("${resource-path.profile-image-path}")
    private String IMAGE_PATH;

    @Test
    @DisplayName("이미지 저장 성공 테스트")
    void successSaveTest() throws Exception {
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));
        //when
        String save = profileImageServiceImpl.saveImage(accountId, image);
        ProfileImage findProfile = profileImageServiceImpl.findByAccountId(accountId);
        File file = new File(generator.generatedFullPath(IMAGE_PATH, findProfile.getStoredImageName()));

        //then
        assertThat(findProfile.getAccountId()).isEqualTo(1L);
        assertThat(findProfile.getUploadImageName()).isEqualTo(image.getOriginalFilename());
        assertThat(SEARCH_PROFILE_PATTERN + findProfile.getStoredImageName()).isEqualTo(save);
        assertThat(file.exists()).isTrue();
        file.delete();
        assertThat(file.exists()).isFalse();

    }

    @Test
    @DisplayName("프로필 이미지 삭제 테스트")
    void deleteProfileTest() throws Exception {
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));
        profileImageServiceImpl.saveImage(accountId, image);

        ProfileImage findBeforeDelete = profileImageRepository.findByAccountId(accountId).get();
        File file = new File(generator.generatedFullPath(IMAGE_PATH, findBeforeDelete.getStoredImageName()));
        //when
        String result = profileImageServiceImpl.deleteProfileImage(accountId);
        ProfileImage find = profileImageServiceImpl.findByAccountId(accountId);
        //then
        assertThat(result).isEqualTo(SEARCH_DEFAULT_PATTERN + find.getStoredImageName());
        assertThat(file.exists()).isFalse();
        assertThat(find.getUploadImageName()).isNull();
        assertThat(find.getStoredImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateProfileTest() throws Exception {
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile newImage = new MockMultipartFile("image", "test-newImage.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));
        //when
        String beforeUpdate = profileImageServiceImpl.saveImage(accountId, image);
        ProfileImage findBefore = profileImageRepository.findByAccountId(1L).get();
        File beforeFile = new File(generator.generatedFullPath(IMAGE_PATH, findBefore.getStoredImageName()));
        String afterUpdate = profileImageServiceImpl.updateProfileImage(1L, newImage);
        ProfileImage findAfter = profileImageRepository.findByAccountId(1L).get();
        File afterFile = new File(generator.generatedFullPath(IMAGE_PATH, findAfter.getStoredImageName()));


        //then
        assertThat(afterUpdate).isNotEqualTo(beforeUpdate);
        assertThat(afterUpdate).isEqualTo(SEARCH_PROFILE_PATTERN + findAfter.getStoredImageName());
        assertThat(findAfter.getUploadImageName()).isEqualTo(newImage.getOriginalFilename());
        assertThat(beforeFile.exists()).isFalse();
        assertThat(afterFile.getName()).isEqualTo(findAfter.getStoredImageName());

    }

    @Test
    @DisplayName("이미지 바이트로 받기 테스트")
    void downloadTest() throws Exception{
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));
        profileImageServiceImpl.saveImage(accountId,image);
        //when
        byte[] imageByByte = profileImageServiceImpl.getImageByByte(accountId);
        //then
        assertThat(imageByByte).isEqualTo(image.getBytes());
    }


}