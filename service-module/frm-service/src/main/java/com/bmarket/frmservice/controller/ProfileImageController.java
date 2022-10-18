package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.service.ProfileImageServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageServiceV1 profileImageServiceV1;

    @PostMapping(value = "/frm/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createProfileImage(@RequestPart(name = "image") MultipartFile image) {
        log.info("createProfileImage");

        log.info("original image name ={}", image.getOriginalFilename());
        log.info("stored image name ={}", image.getOriginalFilename());

        String result = profileImageServiceV1.save(image);
        return result;
    }

    @GetMapping("/frm/profile/{imageId}")
    public ResponseEntity getProfileImage(@PathVariable String imageId) throws IOException {
        byte[] image = profileImageServiceV1.findImage(imageId);

        return ResponseEntity.ok().body("test");

    }
}
