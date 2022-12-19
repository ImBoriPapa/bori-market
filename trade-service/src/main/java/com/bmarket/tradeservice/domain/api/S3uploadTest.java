package com.bmarket.tradeservice.domain.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class S3uploadTest {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public String post(@RequestPart("images") MultipartFile multipartFile) throws IOException {

        return uploadService.upload(multipartFile, "bori-market-bucket", "image");
    }


}
