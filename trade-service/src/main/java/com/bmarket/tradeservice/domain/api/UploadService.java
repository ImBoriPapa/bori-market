package com.bmarket.tradeservice.domain.api;

import com.bmarket.tradeservice.domain.dto.UploadImageDetail;
import com.bmarket.tradeservice.utils.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    private String bucket = "bori-market-bucket";

    private final S3ImageUploader s3ImageUploader;

    public List<UploadImageDetail> uploadFile(List<MultipartFile> multipartFile) {
        return s3ImageUploader.uploadFile(multipartFile, "trade");
    }
}