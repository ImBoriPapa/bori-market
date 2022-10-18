package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.ProfileImage;
import com.bmarket.frmservice.repository.ProfileImageRepository;
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
    @Value("${resource-path.path-pattern}")
    private String searchPattern;

    private final ProfileImageRepository profileImageRepository;

    public String save(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();

        String extension = getExtension(originalFilename);

        String storedName = generateStoredName(extension);

        String fullPath = generatedFullPath(path,storedName);

        try {
            image.transferTo(new File(fullPath));
        } catch (IOException e) {
            log.error("error", e);
        }

        ProfileImage profileImage = ProfileImage.createProfileImage()
                .uploadImageName(originalFilename)
                .storedImageName(storedName)
                .size(image.getSize())
                .build();

        ProfileImage save = profileImageRepository.save(profileImage);
        String path = searchPattern+save.getStoredImageName();
        return path;
    }

    public boolean deleteProfileImage(String id){
        Optional<ProfileImage> profileImage = profileImageRepository.findById(id);
        ProfileImage image = profileImage.get();
        String storedImageName = image.getStoredImageName();

        File file = new File(generatedFullPath(path, storedImageName));

        if(file.exists()){
            boolean delete = file.delete();
            profileImageRepository.delete(image);
            return delete;
        }
        return false;
    }

    public String updateProfileImage(String id,MultipartFile newImages){
        Optional<ProfileImage> byId = profileImageRepository.findById(id);
        ProfileImage image = byId.get();
        deleteProfileImage(id);

        String save = save(newImages);

        return save;
    }

    public byte[] findImage(String imageId) throws IOException {
        Optional<ProfileImage> image = profileImageRepository.findById(imageId);
        ProfileImage profileImage = image.get();
        String storedImageName = profileImage.getStoredImageName();

        String fullPath = generatedFullPath(path, storedImageName);
        byte[] allBytes = Files.readAllBytes(new File(fullPath).toPath());

        return allBytes;
    }

    private static String generatedFullPath(String path, String storedName) {
        return path + storedName;

    }

    private static String generateStoredName(String ext) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String getExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
