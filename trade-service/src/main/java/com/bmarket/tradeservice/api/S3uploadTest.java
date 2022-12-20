package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.dto.ImageDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class S3uploadTest {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity post(@RequestPart("images") List<MultipartFile> multipartFiles) throws IOException {

        List<ImageDetailDto> detailList = uploadService.uploadFile(multipartFiles);
        return ResponseEntity.ok().body(detailList);
    }


}
