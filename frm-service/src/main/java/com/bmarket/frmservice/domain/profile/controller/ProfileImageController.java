package com.bmarket.frmservice.domain.profile.controller;

import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/internal")
public class ProfileImageController {
    private final ProfileImageServiceImpl profileImageServiceImpl;

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
    public ResponseEntity putProfileImage(@PathVariable Long accountId,
                                          @RequestPart(name = "image", required = false) MultipartFile image) {

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(accountId, image);

        return ResponseEntity.ok().body(responseProfile);
    }

    /**
     * 계정 아이디로 프로필 이미지 삭제
     */
    @DeleteMapping("/frm/account/{accountId}/profile")
    public ResponseEntity deleteProfileImage(@PathVariable Long accountId) {

        ResponseProfile responseProfile = profileImageServiceImpl.deleteProfileImage(accountId);

        return ResponseEntity.ok().body(responseProfile);
    }

}
