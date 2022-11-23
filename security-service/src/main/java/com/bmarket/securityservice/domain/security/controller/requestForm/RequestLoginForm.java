package com.bmarket.securityservice.domain.security.controller.requestForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.bmarket.securityservice.utils.regex.Regex.ENG_NUMBER;
import static com.bmarket.securityservice.utils.regex.Regex.PASSWORD;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginForm {

    @NotBlank(message = "로그인 아이디는 받드시 입력해야 합니다.")
    @Size(min = 6,max = 10,message = "로그인 아이디는 6~15자리이어야 합니다.")
    @Pattern(regexp = ENG_NUMBER,message = "로그인 아이디는 영문자 or 영문자+숫자만 허용합니다.")
    String loginId;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    @Pattern(regexp = PASSWORD,message = "비밀 번호는 숫자,문자,특수문자 포함 8~15자리")
    String password;
}
