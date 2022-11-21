package com.bmarket.securityservice.domain.profile.controller;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.common.ResponseForm;
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
@RequestMapping("/account/{accountId}/profile")
public class ProfileController {
    // TODO: 2022/11/16 validation test
    // TODO: 2022/11/16 변경시 연관된 서비스에 변경내용 전파 구현
    // TODO: 2022/11/16 header 에 clientId 응답 추가
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    /**
     * 프로필 단건 조회
     */
    // TODO: 2022/11/16 링크, header 추가
    @GetMapping()
    public ResponseEntity getProfile(@PathVariable Long accountId) {
        ProfileResultForm.profileResult profile = profileQueryService.getProfile(accountId);

        EntityModel<ProfileResultForm.profileResult> entityModel = EntityModel.of(profile);

        List<Link> links = getProfileLinks();

        entityModel.add(links);


        return ResponseEntity.ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    private static List<Link> getProfileLinks() {
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash("/nickname").withRel("PUT : nickname 변경");
        Link link2 = webMvcLinkBuilder.slash("/image").withRel("PUT : image 변경");
        Link link3 = webMvcLinkBuilder.slash("/address").withRel("PUT : 주소 변경");
        Link link4 = webMvcLinkBuilder.slash("/range").withRel("PUT : 주소검색 범위 변경");
        List<Link> links = List.of(link1, link2, link3, link4);
        return links;
    }

    /**
     * 닉네임 수정 닉네임 리소스만 때문에 PutMapping
     */
    // TODO: 2022/11/16 헤더 추가
    @PutMapping("/nickname")
    public ResponseEntity putNickname(@Validated @PathVariable Long accountId,
                                      @RequestBody RequestProfileForm.UpdateNickname form,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        profileCommandService.updateNickname(accountId, form.getNickname());

        List<Link> links = getProfileLinks();


        return ResponseEntity.ok().body(new ResponseForm.Of(ResponseStatus.SUCCESS, links));
    }

    @PutMapping("/range")
    public ResponseEntity putAddressSearchRange(@PathVariable Long accountId
            , @RequestParam AddressRange addressRange) {
        profileCommandService.updateAddressSearchRange(accountId, addressRange);

        List<Link> links = getProfileLinks();

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, links));
    }

    /**
     * 주소 수정
     */
    // TODO: 2022/11/16 헤더 추가
    @PutMapping("/address")
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

        List<Link> links = getProfileLinks();



        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, links));
    }

    /**
     * 프로필 이미지 변경
     */
    @PutMapping("/image")
    public ResponseEntity putImage(@PathVariable Long accountId,
                                   @RequestPart MultipartFile image) {

        profileCommandService.updateProfileImage(accountId, image);

        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);

        List<Link> links = getProfileLinks();


        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, links));
    }

}
