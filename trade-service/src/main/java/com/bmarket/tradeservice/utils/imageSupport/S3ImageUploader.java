package com.bmarket.tradeservice.utils.imageSupport;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bmarket.tradeservice.dto.ImageDetailDto;
import com.bmarket.tradeservice.exception.custom_exception.FileUploadException;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!dev")
public class S3ImageUploader implements ImageUploader {

    private String bucket = "bori-market-bucket";
    private String path = "https://bori-market-bucket.s3.ap-northeast-2.amazonaws.com/";
    private final AmazonS3Client amazonS3Client;

    @Override
    public List<ImageDetailDto> uploadFile(List<MultipartFile> images, String folderName) {
        log.info("[S3ImageUploader -> uploadFile()]");
        ArrayList<ImageDetailDto> details = new ArrayList<>();

        images.forEach(file -> {
            String storedName = getStoredName(folderName, file);

            putImage(file, storedName);

            details.add(ImageDetailDto
                    .builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedName)
                    .fullPath(path + storedName)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build());
        });
        return details;
    }

    /**
     * 폴더 경로 포함 저장 위치
     */
    private String getStoredName(String folderName, MultipartFile file) {
        return folderName + File.separator + createFileName(file.getOriginalFilename());
    }

    /**
     * S3 이미지 저장소에 업로드
     */
    private void putImage(MultipartFile file, String storedName) {

        ObjectMetadata objectMetadata = getObjectMetadata(file);

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, storedName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FileUploadException(ResponseStatus.UPLOAD_FAIL_ERROR);
        }
    }

    /**
     * MultipartFile 로 메타 데이터 생성
     */
    private static ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    @Override
    public String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    @Override
    public String getFileExtension(String originalFileName) {

        hasExtension(originalFileName);

        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        isSupportExtension(ext);

        return ext;
    }

    /**
     * 확장자가 있는 파일인지 확인
     */
    private void hasExtension(String originalFileName) {
        if (originalFileName.lastIndexOf(".") < 0) {
            log.info("[확장자가 없는 파일명={}]", originalFileName);
            throw new FileUploadException(ResponseStatus.WRONG_FILE_ERROR);
        }
    }

    /**
     * 지원하는 이미지 형식인지 확인
     */
    private void isSupportExtension(String ext) {
        Arrays.stream(SupportImageExtension.values())
                .filter(d -> d.getExtension().equals(ext.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new FileUploadException(ResponseStatus.NOT_SUPPORT_ERROR));
    }

    @Override
    public void deleteFile(String storedName) {
        log.info("[deleteFile]");
        amazonS3Client.deleteObject(new DeleteObjectRequest(path, storedName));
    }

    @Override
    public void deleteFile(List<String> storedNameList) {
        log.info("[deleteFile]");
        storedNameList.forEach(list -> new DeleteObjectRequest(bucket, list));
    }
}
