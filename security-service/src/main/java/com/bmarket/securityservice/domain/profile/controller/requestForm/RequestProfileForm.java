package com.bmarket.securityservice.domain.profile.controller.requestForm;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@Getter
public class RequestProfileForm {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNickname {
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEmail{
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateContact{
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
        private Integer addressCode;
        private String city;
        private String district;
        private String town;
    }

}
