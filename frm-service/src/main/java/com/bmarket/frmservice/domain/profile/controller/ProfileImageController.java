package com.bmarket.frmservice.domain.profile.controller;

import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.bmarket.frmservice.utils.Patterns.SEARCH_DEFAULT_PATTERN;
import static com.bmarket.frmservice.utils.Patterns.SEARCH_PROFILE_PATTERN;

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
    @PostMapping(value = "/frm/account/{accountId}/profile")
    public ResponseEntity postProfileImage(@PathVariable(name = "accountId") Long id) {

        log.info("[createProfileImage]");
        log.info("accountId= {}", id);

        ResponseProfile profileImage = profileImageServiceImpl.createProfileImage(id);

        return ResponseEntity.ok().body(profileImage);
    }


    /**
     * 프로필 이미지 수정
     */
    @PutMapping("/frm/account/{accountId}/profile")
    public ResponseEntity updateProfileImage(@PathVariable Long accountId,
                                             @RequestPart(name = "image", required = false) MultipartFile image) {

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(accountId, image);

        return ResponseEntity.ok().body(responseProfile);
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/frm/profile/{accountId}")
    public String deleteProfileImage(@PathVariable Long accountId) {
        return profileImageServiceImpl.deleteProfileImage(accountId);
    }


}
