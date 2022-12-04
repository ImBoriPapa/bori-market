package com.bmarket.securityservice.domain.profile.controller;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.exception.exception_controller.ResponseForm;
import com.bmarket.securityservice.domain.profile.service.ProfileCommandService;
import com.bmarket.securityservice.domain.profile.service.ProfileQueryService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
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
@RequestMapping("/account")
public class ProfileController {
    // TODO: 2022/11/16 validation test
    // TODO: 2022/11/16 변경시 연관된 서비스에 변경내용 전파 구현
    // TODO: 2022/11/16 header 에 clientId 응답 추가
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    /**
     * 프로필 단건 조회
     */
    // TODO: 2022/11/16 header 추가
    @GetMapping("/{accountId}/profile")
    public ResponseEntity getProfile(@PathVariable Long accountId) {
        ProfileResultForm.ProfileResult profile = profileQueryService.getProfile(accountId);

        EntityModel<ProfileResultForm.ProfileResult> entityModel = EntityModel.of(profile);

        List<Link> links = getProfileLinks(accountId);

        entityModel.add(links);


        return ResponseEntity.ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    private static List<Link> getProfileLinks(Long accountId) {
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(ProfileController.class);
        Link link1 = webMvcLinkBuilder.slash(accountId).slash("/nickname").withRel("PATCH : nickname 변경");
        Link link2 = webMvcLinkBuilder.slash(accountId).slash("/image").withRel("PATCH : image 변경");
        Link link3 = webMvcLinkBuilder.slash(accountId).slash("/address").withRel("PATCH : 주소 변경");
        Link link4 = webMvcLinkBuilder.slash(accountId).slash("/range").withRel("PATCH : 주소검색 범위 변경");
        List<Link> links = List.of(link1, link2, link3, link4);
        return links;
    }

    /**
     * 닉네임 수정 닉네임 리소스만 때문에 PutMapping
     */
    // TODO: 2022/11/16 헤더 추가,반환값 추가
    @PatchMapping("/{accountId}/profile/nickname")
    public ResponseEntity patchNickname(@Validated @PathVariable Long accountId,
                                        @RequestBody RequestProfileForm.UpdateNickname form,
                                        BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        Long nickname = profileCommandService.updateNickname(accountId, form);


        return ResponseEntity.ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, EntityModel.of(new ProfileResultForm.ProfileUpdateResult(nickname)).add(getProfileLinks(accountId))));
    }

    @PatchMapping("/{accountId}/profile/range")
    public ResponseEntity patchAddressSearchRange(@Validated @PathVariable Long accountId
            , @RequestBody RequestProfileForm.UpdateRange form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION, bindingResult);
        }

        EntityModel<ProfileResultForm.ProfileUpdateResult> entityModel = EntityModel
                .of(new ProfileResultForm.ProfileUpdateResult(profileCommandService.updateAddressSearchRange(accountId, form.getAddressRange())));

        entityModel.add(getProfileLinks(accountId));

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 주소 수정
     */
    // TODO: 2022/11/16 헤더 추가
    @PatchMapping("/{accountId}/profile/address")
    public ResponseEntity patchAddress(@Validated
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


        EntityModel<ProfileResultForm.ProfileUpdateResult> entityModel = EntityModel
                .of(new ProfileResultForm.ProfileUpdateResult(profileCommandService.updateAddress(accountId, address)));

        entityModel.add(getProfileLinks(accountId));

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 프로필 이미지 변경
     */
    @PatchMapping("/{accountId}/profile/image")
    public ResponseEntity patchImage(@PathVariable Long accountId,
                                     @RequestPart MultipartFile image) {
        // TODO: 2022/11/23 아래 상황 별 대처 구현
        /**
         * 상황 1. 이미지가 정상
         * 상황 2. 이미지가 여러개일 경우
         * 상황 3. 이미지가 null 일 경우
         */


        EntityModel<ProfileResultForm.ProfileUpdateResult> entityModel = EntityModel.of(new ProfileResultForm.ProfileUpdateResult(profileCommandService.updateProfileImage(accountId, image)));
        entityModel.add(getProfileLinks(accountId));

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }


}
