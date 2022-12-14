package com.bmarket.frmservice.controller.profile;

import com.bmarket.frmservice.domain.profile.dto.ResponseProfile;
import com.bmarket.frmservice.domain.profile.service.ProfileImageServiceImpl;
import lombok.RequiredArgsConstructor;
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
     * 프로필 이미지 생성 및 경로 반환
     */
    @PostMapping(value = "/frm/profile")
    public ResponseEntity postProfileImage() {

        log.info("[createProfileImage]");

        ResponseProfile profileImage = profileImageServiceImpl.createProfileImage();

        return ResponseEntity.ok().body(profileImage);
    }


    /**
     * 프로필 이미지 수정
     */
    @PutMapping("/frm/profile/{imageId}")
    public ResponseEntity putProfileImage(@PathVariable(name = "imageId") String id,
                                          @RequestPart(name = "image", required = false) MultipartFile image) {

        ResponseProfile responseProfile = profileImageServiceImpl.updateProfileImage(id, image);

        return ResponseEntity.ok().body(responseProfile);
    }

    /**
     * 계정 아이디로 프로필 이미지 삭제
     */
    @DeleteMapping("/frm/profile/{imageId}")
    public ResponseEntity deleteProfileImage(@PathVariable(name = "imageId") String id) {

        ResponseProfile responseProfile = profileImageServiceImpl.deleteProfileImage(id);

        return ResponseEntity.ok().body(responseProfile);
    }

}
