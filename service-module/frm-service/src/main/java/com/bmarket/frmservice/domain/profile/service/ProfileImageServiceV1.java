package com.bmarket.frmservice.domain.profile.service;

import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.utils.ImageNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


import static com.bmarket.frmservice.utils.Patterns.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileImageServiceV1 {

    @Value("${resource-path.profile-image-path}")
    private String IMAGE_PATH;
    private final ProfileImageRepository profileImageRepository;
    private final ImageNameGenerator generator;

    /**
     * @param accountId
     * @param image
     * @return
     */
    public String save(Long accountId, MultipartFile image) {
        String originalFilename = image.getOriginalFilename();

        String extension = generator.getExtension(originalFilename);

        String storedName = generator.generateStoredName(extension);

        String fullPath = generator.generatedFullPath(IMAGE_PATH, storedName);

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

    public String deleteProfileImage(Long accountId) {
        ProfileImage profileImage = profileImageRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 이미지가 존재하지 않습니다."));

        String storedImageName = profileImage.getStoredImageName();

        File target = new File(generator.generatedFullPath(IMAGE_PATH, storedImageName));

        if (!target.exists()) {
            return "delete-fail";
        }
        target.delete();
        profileImage.deleteProfileImage(DEFAULT_IMAGE_NAME);
        ProfileImage image = profileImageRepository.save(profileImage);
        return SEARCH_DEFAULT_PATTERN + image.getStoredImageName();
    }

    public String updateProfileImage(Long accountId, MultipartFile newImages) {
        ProfileImage profileImage = profileImageRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 이미지가 존재하지 않습니다."));

        File before = new File(generator.generatedFullPath(IMAGE_PATH, profileImage.getStoredImageName()));

        if (!before.exists()) {
            throw new IllegalArgumentException("저장된 프로필 이미지가 없습니다.");
        }
        before.delete();

        String originalFilename = newImages.getOriginalFilename();

        String extension = generator.getExtension(originalFilename);

        String storedName = generator.generateStoredName(extension);

        String fullPath = generator.generatedFullPath(IMAGE_PATH, storedName);

        try {
            newImages.transferTo(new File(fullPath));
        } catch (IOException e) {
            log.error("error", e);
        }

        profileImage.updateProfileImage(originalFilename, storedName);
        ProfileImage save = profileImageRepository.save(profileImage);

        return SEARCH_PATTERN + save.getStoredImageName();
    }

    public byte[] getImageByByte(Long accountId) {
        ProfileImage image = profileImageRepository.findByAccountId(accountId)
                .orElseThrow(()->new IllegalArgumentException("이지를 찾을수 없습니다."));

        String storedImageName = image.getStoredImageName();

        String fullPath = generator.generatedFullPath(IMAGE_PATH, storedImageName);
        byte[] allBytes;

        try {
            allBytes = Files.readAllBytes(new File(fullPath).toPath());
        }catch (IOException e){
            throw new IllegalArgumentException("이미지 불러오기 실패");
        }

        return allBytes;
    }

    public ProfileImage findByAccountId(Long accountId) {
        return profileImageRepository.findByAccountId(accountId).orElseThrow(() -> new IllegalArgumentException("해당 계정 아이디로 이미지를 찾을수 없습니다."));
    }

    public String findDefaultImage() {
        return SEARCH_DEFAULT_PATTERN + DEFAULT_IMAGE_NAME;
    }


}
