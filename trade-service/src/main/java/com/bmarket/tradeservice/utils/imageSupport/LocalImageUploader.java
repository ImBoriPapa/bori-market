package com.bmarket.tradeservice.utils.imageSupport;

import com.bmarket.tradeservice.dto.ImageDetailDto;
import com.bmarket.tradeservice.exception.custom_exception.FileUploadException;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
@Profile("dev")
public class LocalImageUploader implements ImageUploader {
    @Value("${file.dir}")
    private String dir;

    @Override
    public List<ImageDetailDto> uploadFile(List<MultipartFile> images, String folderName) {
        log.info("[LocalImageUploader -> uploadFile()]");

        List<ImageDetailDto> list = new ArrayList<>();
        for (MultipartFile file : images) {

            String extension = getFileExtension(file.getOriginalFilename());
            String storedName = UUID.randomUUID() + extension;
            String fullPath = dir + storedName;

            fileTransfer(file, fullPath);

            ImageDetailDto imageDetailDto = ImageDetailDto.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedName)
                    .fullPath(fullPath)
                    .size(file.getSize())
                    .fileType(file.getContentType())
                    .build();

            list.add(imageDetailDto);
        }
        return list;
    }

    private void fileTransfer(MultipartFile file, String fullPath) {
        log.info("[LocalImageUploader -> fileTransfer]");
        try {
            file.transferTo(new File(fullPath));
        } catch (IOException e) {
            log.error("[ERROR MESSAGE= {}]", e.getMessage());
            throw new IllegalArgumentException("파일 저장에 실패했습니다.");
        }
    }

    @Override
    public String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    @Override
    public String getFileExtension(String originalFileName) {

        if (originalFileName.lastIndexOf(".") < 0) {
            log.info("[확장자가 없는 파일명={}]", originalFileName);
            throw new FileUploadException(ResponseStatus.WRONG_FILE_ERROR);
        }
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        return Arrays.stream(SupportImageExtension.values())
                .filter(d -> d.getExtension().equals(ext))
                .findFirst()
                .orElseThrow(() -> new FileUploadException(ResponseStatus.NOT_SUPPORT_ERROR)).getExtension();
    }

    @Override
    public void deleteFile(String storedName) {
        log.info("[IMAGE DELETE imageName= {}]", storedName);
        File target = new File(dir + storedName);
        target.delete();
    }

    @Override
    public void deleteFile(List<String> storedNameList) {
        log.info("[IMAGE DELETE imageNameList]");
        storedNameList.forEach(data -> new File(dir, data).delete());
    }
}
