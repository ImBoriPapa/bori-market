package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.ProfileImage;
import com.bmarket.frmservice.repository.ProfileImageRepository;
import com.bmarket.frmservice.utils.ImageNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;

import java.io.IOException;
import java.nio.file.Files;


import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileImageServiceV1 {

    @Value("${resource-path.profile-image-path}")
    private String path;
    private static final String SEARCH_PATTERN = "http://localhost:8095/file/";
    private final ProfileImageRepository profileImageRepository;
    private final ImageNameGenerator generator;
    public String save(Long accountId, MultipartFile image) {
        String originalFilename = image.getOriginalFilename();

        String extension = generator.getExtension(originalFilename);

        String storedName = generator.generateStoredName(extension);

        String fullPath = generator.generatedFullPath(path, storedName);

        try {
            image.transferTo(new File(fullPath));
        } catch (IOException e) {
            log.error("error", e);
        }

        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(accountId)
                .uploadImageName(originalFilename)
                .storedImageName(storedName)
                .size(image.getSize())
                .build();

        ProfileImage save = profileImageRepository.save(profileImage);
        String path = SEARCH_PATTERN + save.getStoredImageName();
        return path;
    }

    public boolean deleteProfileImage(String id) {
        Optional<ProfileImage> profileImage = profileImageRepository.findById(id);
        ProfileImage image = profileImage.get();
        String storedImageName = image.getStoredImageName();

        File file = new File(generator.generatedFullPath(path, storedImageName));

        if (file.exists()) {
            boolean delete = file.delete();
            profileImageRepository.delete(image);
            return delete;
        }
        return false;
    }

    public String updateProfileImage(String id, MultipartFile newImages) {
        Optional<ProfileImage> byId = profileImageRepository.findById(id);
        ProfileImage image = byId.get();
        deleteProfileImage(id);
        return "";
    }

    public byte[] findImage(String imageId) throws IOException {
        Optional<ProfileImage> image = profileImageRepository.findById(imageId);
        ProfileImage profileImage = image.get();
        String storedImageName = profileImage.getStoredImageName();

        String fullPath = generator.generatedFullPath(path, storedImageName);
        byte[] allBytes = Files.readAllBytes(new File(fullPath).toPath());

        return allBytes;
    }

    public ProfileImage findByAccountId(Long accountId){
        return profileImageRepository.findByAccountId(accountId).orElseThrow(() -> new IllegalArgumentException("해당 계정 아이디로 이미지를 찾을수 없습니다."));
    }

    public String getDefaultImage() {
        return "http://localhost:8095/file/default/dafault-image";
    }







}
