package com.bmarket.securityservice.account.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import static com.bmarket.securityservice.security.constant.Regex.*;


@Getter
@NoArgsConstructor
public class RequestAccountForm {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateForm{

        @NotBlank(message = "이름은 반드시 입력해야 합니다.")
        private String name;

        @NotBlank(message = "닉네임은 반드시 입력해야 합니다")
        @Size(min = 2,max = 10,message = "닉네임은 2~10자리 이어야 합니다.")
        private String nickname;

        @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
        @Pattern(regexp = PASSWORD,message = "비밀 번호는 숫자,문자,특수문자 포함 8~15자리")
        private String password;

        @Email(message = "잘못된 이메일 양식입니다.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;

        @NotBlank(message = "연락처는 필수 입력 사항입니다.")
        @Pattern(regexp = NUMBER,message = "연락처는 숫자만 입력하세요. 입력 예)01012341234")
        private String contact;

        @Min(value = 1000,message = "잘못된 주소 코드입니다.")
        private Integer addressCode;

        @NotBlank(message = "도시를 입력하세요")
        private String city;

        @NotBlank(message = "지역구를 입력하세요")
        private String district;

        @NotBlank(message = "동네를 입력하세요")
        private String town;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteForm{
        private String password;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdatePasswordForm {

        @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
        @Pattern(regexp = PASSWORD,message = "비밀 번호는 숫자,문자,특수문자 포함 8~15자리")
        private String password;

        @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
        @Pattern(regexp = PASSWORD,message = "비밀 번호는 숫자,문자,특수문자 포함 8~15자리")
        private String newPassword;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateEmailForm{
        @Email(message = "잘못된 이메일 양식입니다.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;
    }

}
