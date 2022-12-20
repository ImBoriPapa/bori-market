package com.bmarket.tradeservice.utils;

import com.bmarket.tradeservice.domain.dto.UploadImageDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    public List<UploadImageDetail> uploadFile(List<MultipartFile> images, String folderName);
    public String createFileName(String originalFileName);
    public String getFileExtension(String originalFileName);
    public void deleteFile(String filename);
}
