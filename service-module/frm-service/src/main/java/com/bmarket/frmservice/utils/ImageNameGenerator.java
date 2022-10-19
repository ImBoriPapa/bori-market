package com.bmarket.frmservice.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageNameGenerator {

    public String getExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public String generateStoredName(String ext) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    public String generatedFullPath(String path, String storedName) {
        return path + storedName;

    }
}
