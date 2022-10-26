package com.bmarket.frmservice.utils;

import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileManager {

    private String extension;
    private String storedName;
    private String fullPath;

    private String getExtension(String originalFilename) {
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

    public void deleteFile(String path,String storedName){
        File target = new File(generatedFullPath(path, storedName));
        if(!target.exists()){
            throw new IllegalArgumentException("삭제할 파일이 존재하지 않습니다.");
        }
        target.delete();
    }
}
