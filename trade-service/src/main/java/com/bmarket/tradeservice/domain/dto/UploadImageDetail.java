package com.bmarket.tradeservice.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadImageDetail {
    private String originalFileName;
    private String storedFileName;
    private String fullPath;
    private Long size;
    private String fileType;

    @Builder
    public UploadImageDetail(String originalFileName, String storedFileName, String fullPath, Long size, String fileType) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fullPath = fullPath;
        this.size = size;
        this.fileType = fileType;
    }
}
