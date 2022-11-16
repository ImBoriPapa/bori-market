package com.bmarket.securityservice.domain.profile.controller;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.profile.controller.requestForm.RequestProfileForm;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.service.ProfileCommandService;
import com.bmarket.securityservice.domain.profile.service.ProfileQueryService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    // TODO: 2022/11/16 validation test
    // TODO: 2022/11/16 변경시 연관된 서비스에 변경내용 전파 구현
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    /**
     * 프로필 단건 조회
     */
    // TODO: 2022/11/16 링크, header 추가
    @GetMapping(value = "/profile/{accountId}")
    public ResponseEntity getProfile(@PathVariable Long accountId) {
        ProfileResultForm.profileResult profile = profileQueryService.getProfile(accountId);
        EntityModel<ProfileResultForm.profileResult> entityModel = EntityModel.of(profile);

        return ResponseEntity.ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 닉네임 수정 닉네임 리소스만 때문에 PutMapping
     */
    // TODO: 2022/11/16 헤더 추가
    @PutMapping("/profile/{accountId}/nickname")
    public ResponseEntity putNickname(@Validated @PathVariable Long accountId,
                                      @RequestBody RequestProfileForm.UpdateNickname form,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        profileCommandService.updateNickname(accountId, form.getNickname());

        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/profile").slash(accountId).withRel("GET : profile");
        Link link2 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/image").withRel("PUT : 프로필 이미지 변경");
        List<Link> linkList = List.of(link1, link2);
        return ResponseEntity.ok().body(new ResponseForm.Of(ResponseStatus.SUCCESS, linkList));
    }

    @PutMapping("/profile/{accountId}/range")
    public ResponseEntity putAddressSearchRange(@PathVariable Long accountId
            , @RequestParam AddressRange addressRange) {
        profileCommandService.updateAddressSearchRange(accountId, addressRange);
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/profile").slash(accountId).withRel("GET : profile");
        Link link2 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/image").withRel("PUT : 프로필 이미지 변경");
        List<Link> linkList = List.of(link1, link2);
        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, linkList));
    }

    /**
     * 주소 수정
     */
    // TODO: 2022/11/16 헤더 추가
    @PutMapping("/profile/{accountId}/address")
    public ResponseEntity putAddress(@Validated
                                     @PathVariable Long accountId,
                                     @RequestBody RequestProfileForm.UpdateAddress form,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        Address address = Address.createAddress()
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .town(form.getTown()).build();
        profileCommandService.updateAddress(accountId, address);

        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/profile").slash(accountId).withRel("GET : profile");
        Link link2 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/image").withRel("PUT : 프로필 이미지 변경");
        List<Link> linkList = List.of(link1, link2);

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, linkList));
    }

    /**
     * 프로필 이미지 변경
     */
    @PutMapping("/profile/{accountId}/image")
    public ResponseEntity putImage(@PathVariable Long accountId,
                                   @RequestPart MultipartFile image) {

        profileCommandService.updateProfileImage(accountId, image);

        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/profile").slash(accountId).withRel("GET : profile");
        Link link2 = webMvcLinkBuilder.slash("/profile").slash(accountId).slash("/image").withRel("PUT : 프로필 이미지 변경");
        List<Link> linkList = List.of(link1, link2);

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, linkList));
    }

}
