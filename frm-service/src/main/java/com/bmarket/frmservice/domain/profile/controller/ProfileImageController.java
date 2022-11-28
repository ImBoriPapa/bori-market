package com.bmarket.frmservice.domain.profile.controller;

import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageServiceImpl profileImageServiceImpl;

    @GetMapping("/frm/profile/default")
    private String getDefault() {
        return profileImageServiceImpl.findDefaultImage();
    }

    /**
     * 프로필 이미지 저장 및 경로 반환
     */
    @PostMapping(value = "/frm/account/{accountId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createProfileImage(@PathVariable(name = "accountId") Long id,
                                             @RequestPart(name = "image") MultipartFile image) {
        log.info("[createProfileImage]");
        log.info("id={}", id);
        log.info("image ={}", image.getOriginalFilename());

        String save = profileImageServiceImpl.saveImage(id, image);

        return ResponseEntity.ok().body(new ResponseCreateProfileImage(true, save));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseCreateProfileImage {
        private Boolean success;
        private String imagePath;
    }

    /**
     * 프로필 이미지 다운로드 요청
     */
    @GetMapping("/frm/profile/download/{accountId}")
    public ResponseEntity downloadProfileImage(@PathVariable Long accountId) {
        byte[] imageByByte = profileImageServiceImpl.getImageByByte(accountId);

        return ResponseEntity.ok().body(new ResponseDownloadProfileImage(true, imageByByte));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDownloadProfileImage {
        private Boolean success;
        private byte[] imageData;
    }

    /**
     * 계정 아이디로 프로필 이미지 조회
     */
    @GetMapping("/frm/profile/{accountId}")
    public String getProfileImage(@PathVariable Long accountId) {
        ProfileImage profileImage = profileImageServiceImpl.findByAccountId(accountId);
        return profileImage.getStoredImageName();
    }

    /**
     * 프로필 이미지 수정
     */
    @PutMapping("/frm/profile/{accountId}")
    public String updateProfileImage(@PathVariable Long accountId,
                                     @RequestPart(name = "image") MultipartFile image) {

        return profileImageServiceImpl.updateProfileImage(accountId, image);
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/frm/profile/{accountId}")
    public String deleteProfileImage(@PathVariable Long accountId) {
        return profileImageServiceImpl.deleteProfileImage(accountId);
    }
}
