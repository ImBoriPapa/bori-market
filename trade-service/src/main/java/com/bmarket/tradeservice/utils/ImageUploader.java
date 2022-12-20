package com.bmarket.tradeservice.utils;

import com.bmarket.tradeservice.dto.ImageDetailDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    public List<ImageDetailDto> uploadFile(List<MultipartFile> images, String folderName);
    public String createFileName(String originalFileName);
    public String getFileExtension(String originalFileName);
    public void deleteFile(String filename);
}
