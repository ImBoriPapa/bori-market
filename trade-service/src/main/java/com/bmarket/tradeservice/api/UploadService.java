package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.dto.ImageDetailDto;
import com.bmarket.tradeservice.utils.ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    private final ImageUploader imageUploader;
    
    public List<ImageDetailDto> uploadFile(List<MultipartFile> multipartFile) {
        return imageUploader.uploadFile(multipartFile, "trade");
    }
}