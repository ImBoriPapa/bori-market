package com.bmarket.tradeservice.domain.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class S3uploadTest {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public List<String> post(@RequestPart("images") List<MultipartFile> multipartFiles) throws IOException {
        List<String> imagePath = new ArrayList<>();
        String path = "https://bori-market-bucket.s3.ap-northeast-2.amazonaws.com/";
        uploadService.uploadFile(multipartFiles)
                .forEach(name -> imagePath.add(path + name));
        return imagePath;
    }
}
