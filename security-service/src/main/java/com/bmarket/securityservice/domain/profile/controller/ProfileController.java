package com.bmarket.securityservice.domain.profile.controller;

import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.profile.controller.requestForm.RequestProfileForm;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.service.ProfileCommandService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileCommandService profileCommandService;


    @GetMapping(value = "/profile/{clientId}")
    public void getProfile(@PathVariable String clientId, @RequestPart MultipartFile image) {

    }

    @PutMapping("/profile/{accountId}/nickname")
    public ResponseEntity putNickname(@PathVariable Long accountId, @RequestBody RequestProfileForm.UpdateNickname form) {
        profileCommandService.updateNickname(accountId, form.getNickname());


        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/profile").slash(accountId).withRel("GET : profile");
        Link link2 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/email").withRel("PUT : 닉네임 변경");
        Link link3 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/contact").withRel("PUT : 연락처 변경");
        Link link4 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/image").withRel("PUT : 프로필 이미지 변경");
        List<Link> linkList = List.of(link1, link2, link3, link4);
        return ResponseEntity.ok().body(new ResponseForm.Of(ResponseStatus.REQUEST_SUCCESS, linkList));
    }


    @PutMapping("/profile/{clientId}/email")
    public void putEmail(@PathVariable String clientId, @RequestBody RequestProfileForm.UpdateEmail form) {

    }

    @PutMapping("/profile/{clientId}/contact")
    public void putContact(@PathVariable String clientId, @RequestBody RequestProfileForm.UpdateContact form) {

    }

    @PutMapping("/profile/{accountId}/image")
    public void putImage(@PathVariable Long accountId,
                         @RequestPart MultipartFile file) {
        profileCommandService.updateProfileImage(accountId, file);
    }

    @PutMapping("/profile/{accountId}/address-search-range")
    public void putAddressSearchRange(@PathVariable Long accountId, @RequestParam AddressRange addressRange) {
        profileCommandService.updateAddressSearchRange(accountId, addressRange);
    }

}
