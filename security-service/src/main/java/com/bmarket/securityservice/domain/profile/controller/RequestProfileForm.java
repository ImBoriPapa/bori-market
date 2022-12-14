package com.bmarket.securityservice.domain.profile.controller;


import com.bmarket.securityservice.domain.address.AddressRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@Getter
public class RequestProfileForm {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNickname {
        @NotBlank
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEmail{
        @Email
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateContact{
        @NotBlank
        private String contact;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileImage{
        private MultipartFile image;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAddress{
        @Min(1000)
        @Max(5000)
        private Integer addressCode;
        @NotBlank
        private String city;
        @NotBlank
        private String district;
        @NotBlank
        private String town;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRange{
        private AddressRange addressRange;
    }
}
