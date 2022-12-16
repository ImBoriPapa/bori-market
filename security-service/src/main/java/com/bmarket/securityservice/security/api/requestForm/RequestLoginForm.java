package com.bmarket.securityservice.security.api.requestForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.bmarket.securityservice.utils.regex.Regex.PASSWORD;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginForm {

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    @Pattern(regexp = PASSWORD,message = "비밀 번호는 숫자,문자,특수문자 포함 8~15자리")
    String password;
}
