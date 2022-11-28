package com.bmarket.frmservice.utils;

import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 파일 저장시 데이터베이스에 저장할  uploadImageName,storedImageName 파일을 저장 및 경로 반환*
 */
@Component
@Slf4j
public class FileManager {

    private String extension;
    private String storedName;
    private String fullPath;

    /**
     * 확장자 추출
     */
    private String getExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /**
     * uuid + 확장자로 유니크한 저장용 파일명 생성
     */
    private String generateStoredName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 저장 경로 + 저장 파일 이름 으로 전체 경로 생성
     */
    public String generatedFullPath(String path, String storedName) {
        return path + storedName;
    }

    /**
     * MultipartFile 이 한개 인 경우 때 저장 로직
     */
    public UploadFile saveFile(String path, MultipartFile file) {

        extension = getExtension(file.getOriginalFilename());
        storedName = generateStoredName(extension);
        fullPath = generatedFullPath(path, storedName);

        try {
            file.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 저장에 실패 했습니다.");
        }

        return new UploadFile(file.getOriginalFilename(), storedName);
    }
    /**
     * MultipartFile 이 한개 이상일 경우 때 저장 로직
     */
    public List<UploadFile> saveFile(String path, List<MultipartFile> files) {
        List<UploadFile> list = new ArrayList<>();

        for (MultipartFile file : files) {

            extension = getExtension(file.getOriginalFilename());
            storedName = generateStoredName(extension);
            fullPath = generatedFullPath(path, storedName);

            try {
                file.transferTo(new File(fullPath));
            } catch (IOException e) {
                throw new IllegalArgumentException("파일 저장에 실패 했습니다.");
            }
            list.add(new UploadFile(file.getOriginalFilename(), storedName));
        }
        return list;
    }

    /**
     * 저장된 파일 삭제 로직
     */
    public void deleteFile(String path, String storedName) {
        File target = new File(generatedFullPath(path, storedName));
        if (target.exists()) {
            log.info("exists={}", target.exists());
            target.delete();
        }
        throw new IllegalArgumentException("삭제할 파일이 존재하지 않습니다.");
    }

    /**
     * 경로에 저장된 전체 파일 삭제
     */
    public void deleteAllInPath(String path) {
        Arrays.stream(Objects
                        .requireNonNull(new File(path).listFiles()))
                .forEach(File::delete);
    }
}
