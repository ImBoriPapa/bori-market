package com.bmarket.tradeservice.utils;

import com.bmarket.tradeservice.dto.ImageDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@Profile("dev")
public class LocalImageUploader implements ImageUploader {
    @Value("${file.dir}")
    private String dir;
    @Override
    public List<ImageDetailDto> uploadFile(List<MultipartFile> images, String folderName) {
        List<ImageDetailDto> list = new ArrayList<>();

        for (MultipartFile file : images) {

            String extension = getFileExtension(file.getOriginalFilename());
            String storedName = UUID.randomUUID()  + extension;
            String fullPath = dir+storedName;
            log.info("[fullPath= {}]",fullPath);
            try {
                file.transferTo(new File(fullPath));
            } catch (IOException e) {
                throw new IllegalArgumentException("파일 저장에 실패했습니다.");
            }

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

    @Override
    public String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    @Override
    public String getFileExtension(String originalFileName) {
        log.info("originalFileName= {}",originalFileName);
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    @Override
    public void deleteFile(String filename) {

    }
}
