package com.bmarket.tradeservice.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bmarket.tradeservice.dto.ImageDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!dev")
public class S3ImageUploader implements ImageUploader {

    private  String bucket = "bori-market-bucket";
    private  String path = "https://bori-market-bucket.s3.ap-northeast-2.amazonaws.com/";
    private final AmazonS3Client amazonS3Client;

    @Override
    public List<ImageDetailDto> uploadFile(List<MultipartFile> images, String folderName) {

        ArrayList<ImageDetailDto> details = new ArrayList<>();
        images.forEach(file -> {
            String storedName = folderName + File.separator + createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, storedName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new IllegalArgumentException("업로드 실패");
            }

            details.add(ImageDetailDto
                    .builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedName)
                    .fullPath(path+storedName)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build());
        });

        return details;
    }

    @Override
    public String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    @Override
    public String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    @Override
    public void deleteFile(String filename) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
    }


}
