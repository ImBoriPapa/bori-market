package com.bmarket.securityservice.api.account.controller;

import lombok.*;

import javax.validation.constraints.*;

import static com.bmarket.securityservice.utils.regex.Regex.ENG_NUMBER;



@Getter
@NoArgsConstructor
public class RequestAccountForm {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateForm{
        @NotBlank(message = "로그인 아이디는 받드시 입력해야 합니다.")
        @Size(min = 6,max = 10,message = "로그인 아이디는 6~10자리이어야 합니다.")
        @Pattern(regexp = ENG_NUMBER,message = "로그인 아이디는 영문자+숫자만 허용합니다.")
        private String loginId;
        @NotBlank(message = "이름은 반드시 입력해야 합니다.")
        private String name;
        @NotBlank(message = "닉네임은 반드시 입력해야 합니다")
        private String nickname;
        @NotBlank(message = "")
        private String password;
        @Email(message = "")
        @NotBlank(message = "")
        private String email;

        @NotBlank(message = "")
        private String contact;

        @Min(1000)
        private Integer addressCode;

        @NotBlank(message = "")
        private String city;

        @NotBlank(message = "")
        private String district;

        @NotBlank(message = "")
        private String town;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteForm{
        private String password;
    }
}
