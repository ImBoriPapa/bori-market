package com.bmarket.frmservice.domain.profile.service;

import com.bmarket.frmservice.domain.profile.controller.ProfileImageController;
import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.utils.FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import static com.bmarket.frmservice.utils.Patterns.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileImageServiceImpl {

    @Value("${resource-path.profile-image-path}")
    private String IMAGE_PATH;
    private final ProfileImageRepository profileImageRepository;
    private final FileManager manager;

    /**
     * 프로필 이미지 생성
     * 최초 회원 가입이 프로필 이미지는 기본 이미지 경로 반환
     */
    public ResponseProfile createProfileImage(Long accountId) {

        // 계정 아이디로 이미 저장한 프로필 이미지가 존재할 경우
        if (profileImageRepository.findByAccountId(accountId).isPresent()) {
            throw new IllegalArgumentException("이미 프로필 이미지가 존재합니다.");
        }

        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(accountId)
                .storedImageName(DEFAULT_IMAGE_NAME)
                .build();

        profileImageRepository.save(profileImage);

        return ResponseProfile.builder()
                .success(true)
                .accountId(accountId)
                .imagePath(DEFAULT_IMAGE_NAME)
                .build();
    }

    /**
     * 이미지 수정
     * 수정할 이미지가 null 일 경우 default 이미지경로 반환
     */
    public ResponseProfile updateProfileImage(Long accountId, MultipartFile newImages) {

        ProfileImage profileImage = profileImageRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 이미지가 존재하지 않습니다."));

        //수정할 이미지가 null 일 경우 default 이미지경로 반환
        if (newImages == null) {
            return returnDefaultImagePath(profileImage);
        }

        //기존에 저장된 이미지 삭제
        deleteStoredImage(profileImage);

        UploadFile uploadFile = manager.saveFile(IMAGE_PATH, newImages);

        profileImage.updateProfileImage(uploadFile.getUploadName(), uploadFile.getStoredName());

        ProfileImage updatedProfileImage = profileImageRepository.save(profileImage);

        return ResponseProfile.builder()
                .success(true)
                .accountId(updatedProfileImage.getAccountId())
                .imagePath(SEARCH_PROFILE_PATTERN + updatedProfileImage.getStoredImageName())
                .build();
    }

    private ResponseProfile returnDefaultImagePath(ProfileImage profileImage) {
        deleteStoredImage(profileImage);

        profileImage.updateProfileImage(null, DEFAULT_IMAGE_NAME);

        profileImageRepository.save(profileImage);

        return ResponseProfile.builder()
                .success(true)
                .accountId(profileImage.getAccountId())
                .imagePath(SEARCH_DEFAULT_PATTERN + DEFAULT_IMAGE_NAME)
                .build();
    }

    private void deleteStoredImage(ProfileImage profileImage) {
        try {
            manager.deleteFile(IMAGE_PATH, profileImage.getStoredImageName());
        } catch (IllegalArgumentException e) {
            log.info("삭제할 파일이 존재 하지 않습니다. ={}", e.getMessage());
        }
    }

    public String deleteProfileImage(Long accountId) {
        ProfileImage profileImage = profileImageRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 이미지가 존재하지 않습니다."));

        String storedImageName = profileImage.getStoredImageName();

        manager.deleteFile(IMAGE_PATH, storedImageName);

        profileImageRepository.deleteById(profileImage.getId());

        return "ok";
    }

    public ProfileImage findByAccountId(Long accountId) {
        return profileImageRepository.findByAccountId(accountId).orElseThrow(() -> new IllegalArgumentException("해당 계정 아이디로 이미지를 찾을수 없습니다."));
    }

    public String findDefaultImage() {
        return SEARCH_DEFAULT_PATTERN + DEFAULT_IMAGE_NAME;
    }


}
