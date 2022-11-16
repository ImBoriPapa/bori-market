package com.bmarket.securityservice.domain.profile.controller.requestForm;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;


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

}
