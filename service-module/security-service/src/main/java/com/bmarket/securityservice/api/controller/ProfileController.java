package com.bmarket.securityservice.api.controller;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private String path = "";

    private final AccountRepository accountRepository;

    @PostMapping(value = "/account/{clientId}/profile/image",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadProfileImage(@PathVariable String clientId, @RequestPart MultipartFile image) {

        Optional<Account> account = accountRepository.findByClientId(clientId);
        Account targetAccount = account.get();

        String originalFilename = image.getOriginalFilename();

        String ext = getExtension(originalFilename);

        String storedName = generateStoredName(ext);

        String fullPath = generatedFullPath(storedName, path);

        targetAccount.getProfile().upLoadingImage(originalFilename,storedName);

    }

    private static String generatedFullPath(String storedName,String path) {
        String fullPath = storedName + path;
        return fullPath;
    }

    private static String generateStoredName(String ext) {
        String uuid = UUID.randomUUID().toString();
        String storedImageName = uuid + "." + ext;
        return storedImageName;
    }

    private static String getExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");

        String ext = originalFilename.substring(pos + 1);

        return ext;
    }
}
