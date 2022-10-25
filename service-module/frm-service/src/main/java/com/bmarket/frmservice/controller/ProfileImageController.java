package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.domain.ProfileImage;
import com.bmarket.frmservice.service.ProfileImageServiceV1;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageServiceV1 profileImageServiceV1;
    @GetMapping("/frm/profile/default")
    private String getDefault(){
        return profileImageServiceV1.findDefaultImage();
    }
    /**
     * 프로필 이미지 생성 요청
     * @param id
     * @param image
     * @return ResponseEntity.ok , ResponseCreateProfileImage : imagePath
     */
    @PostMapping(value = "/frm/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String createProfileImage(@RequestParam(name = "profileId",defaultValue = "0") Long id,
                                             @RequestPart(name = "image") MultipartFile image) {

        String save = profileImageServiceV1.save(id, image);
        ResponseCreateProfileImage dto = new ResponseCreateProfileImage(true,save);
        return dto.imagePath;
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
     *
     * @param accountId
     * @return byte[]
     * @throws
     */
    @GetMapping("/frm/profile/download/{accountId}")
    public ResponseEntity downloadProfileImage(@PathVariable Long accountId) {
        byte[] imageByByte = profileImageServiceV1.getImageByByte(accountId);

        return ResponseEntity.ok().body(new ResponseDownloadProfileImage(true,imageByByte));
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDownloadProfileImage{
        private Boolean success;
        private byte[] imageData;
    }

    /**
     * 계정 아이디로 프로필 이미지 조회
     *
     * @param accountId
     * @return String 이미지 URL
     */
    @GetMapping("/frm/profile/{accountId}")
    public String getProfileImage(@PathVariable Long accountId) {
        ProfileImage profileImage = profileImageServiceV1.findByAccountId(accountId);
        return profileImage.getStoredImageName();
    }

    /**
     * 프로필 이미지 수정
     *
     * @param accountId
     * @param image     (MultipartFile)
     * @return String 수정된 이미지 URL
     */
    @PutMapping("/frm/profile/{accountId}")
    public String updateProfileImage(@PathVariable Long accountId,
                                     @RequestPart(name = "image") MultipartFile image) {
        return profileImageServiceV1.updateProfileImage(accountId, image);
    }

    /**
     * 프로필 이미지 삭제
     *
     * @param accountId
     * @return default 이미지 URL
     */
    @DeleteMapping("/frm/profile/{accountId}")
    public String deleteProfileImage(@PathVariable Long accountId) {
        return profileImageServiceV1.deleteProfileImage(accountId);
    }
}
