package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.utils.FileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.bmarket.frmservice.utils.Patterns.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class ProfileImageServiceImplTest {

    @Autowired
    ProfileImageServiceImpl profileImageServiceImpl;
    @Autowired
    ProfileImageRepository profileImageRepository;
    @Autowired
    FileManager fileManager;

    @Value("${resource-path.profile-image-path}")
    private String IMAGE_PATH;


    @AfterEach
    void afterEach(){
        profileImageRepository.deleteAll();
        fileManager.deleteAllInPath(IMAGE_PATH);
    }

    @Test
    @DisplayName("이미지 생성 성공 테스트")
    void successSaveTest() throws Exception {
        //given
        Long accountId = 1L;
        //when
        ResponseProfile profileImage = profileImageServiceImpl.createProfileImage(accountId);

        ProfileImage findImage = profileImageRepository.findByAccountId(profileImage.getAccountId())
                .orElseThrow(()->new IllegalArgumentException("존재 하지 안습니다."));
        //then
        assertThat(findImage.getStoredImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateProfileTest() throws Exception {
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));

        //when
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(accountId)
                .storedImageName(DEFAULT_IMAGE_NAME)
                .build();
        profileImageRepository.save(profileImage);

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(1L, image);
        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getAccountId()).isEqualTo(accountId);
        assertThat(responseProfile.getImagePath()).isNotEqualTo(SEARCH_PROFILE_PATTERN + DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트 멀티파트 파일이 없을 경우")
    void updateProfileNoMultipartFileTest() throws Exception {
        //given
        long accountId = 1L;


        //when
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(accountId)
                .storedImageName(DEFAULT_IMAGE_NAME)
                .build();
        profileImageRepository.save(profileImage);

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(1L, null);
        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getAccountId()).isEqualTo(accountId);
        assertThat(responseProfile.getImagePath()).isEqualTo(SEARCH_DEFAULT_PATTERN + DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 삭제 테스트")
    void deleteProfileTest() throws Exception {
        //given
        long accountId = 1L;

        MockMultipartFile image = new MockMultipartFile("image", "testImage.png", "image/png", "image".getBytes());

        UploadFile uploadFile = fileManager.saveFile(IMAGE_PATH, image);

        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(accountId)
                .storedImageName(uploadFile.getStoredName())
                .build();
        ProfileImage save = profileImageRepository.save(profileImage);
        //when
        ResponseProfile responseProfile = profileImageServiceImpl.deleteProfileImage(accountId);

        Optional<ProfileImage> optional = profileImageRepository.findByAccountId(save.getAccountId());

        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getAccountId()).isEqualTo(accountId);
        assertThat(responseProfile.getImagePath()).isNull();
        assertThat(optional.isPresent()).isFalse();
    }


}