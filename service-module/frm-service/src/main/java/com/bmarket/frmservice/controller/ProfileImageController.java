package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.service.ProfileImageServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageServiceV1 profileImageServiceV1;

    @PostMapping(value = "/frm/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String createProfileImage(@RequestPart(name = "accountId") Long id,
                                     @RequestPart(name = "image") MultipartFile image) {
        log.info("createProfileImage");

        log.info("original image name ={}", image.getOriginalFilename());

        String result = profileImageServiceV1.save(id,image);
        log.info("stored image name={}",result);
        return result;
    }

    @GetMapping("/frm/profile/{accountId}")
    public ResponseEntity getProfileImage(@PathVariable Long accountId) throws IOException {
        byte[] image = profileImageServiceV1.getImageByByte(accountId);

        return ResponseEntity.ok().body("test");

    }
}
