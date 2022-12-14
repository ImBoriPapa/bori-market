package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
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

import static com.bmarket.frmservice.utils.AccessUrl.*;
import static org.assertj.core.api.Assertions.assertThat;

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

        //when
        ResponseProfile profileImage = profileImageServiceImpl.createProfileImage();

        ProfileImage findImage = profileImageRepository.findById(profileImage.getImageId())
                .orElseThrow(()->new IllegalArgumentException("존재 하지 안습니다."));
        //then
        assertThat(findImage.getStoredImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateProfileTest() throws Exception {
        //given

        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8));

        //when
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(DEFAULT_IMAGE_NAME)
                .build();
        ProfileImage save = profileImageRepository.save(profileImage);

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(save.getId(), image);
        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getImagePath()).isNotEqualTo(PROFILE_URL + DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트 멀티파트 파일이 없을 경우")
    void updateProfileNoMultipartFileTest() throws Exception {
        //given

        //when
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(DEFAULT_IMAGE_NAME)
                .build();
        ProfileImage save = profileImageRepository.save(profileImage);

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(save.getId(), null);
        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getImageId()).isEqualTo(save.getId());
        assertThat(responseProfile.getImagePath()).isEqualTo(DEFAULT_PROFILE_URL + DEFAULT_IMAGE_NAME);
    }

    @Test
    @DisplayName("프로필 이미지 삭제 테스트")
    void deleteProfileTest() throws Exception {
        //given


        MockMultipartFile image = new MockMultipartFile("image", "testImage.png", "image/png", "image".getBytes());

        UploadFile uploadFile = fileManager.saveFile(IMAGE_PATH, image);

        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(uploadFile.getStoredImageName())
                .build();
        ProfileImage save = profileImageRepository.save(profileImage);
        //when
        ResponseProfile responseProfile = profileImageServiceImpl.deleteProfileImage(save.getId());

        //then
        assertThat(responseProfile.isSuccess()).isTrue();
        assertThat(responseProfile.getImageId()).isNull();
        assertThat(responseProfile.getImagePath()).isNull();

    }


}