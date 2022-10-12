package com.bmarket.securityservice.domain.entity;

import com.fasterxml.uuid.Generators;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;
    @Column(unique = true)
    private String clientId;
    @Column(unique = true)
    private String loginId;
    private String name;
    @Column(unique = true)
    private String nickname;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contact;
    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private boolean isLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginTime;

    /**
     * 계정 생성
     * @param loginId 로그안 아이디
     * @param name    사용자 이름
     * @param nickname 닉네임
     * @param password 비밀번호
     * @param email    이메일
     * @param contact  연락처
     * @param authority 계정 권한
     *                  clientId 사용자 식별 아이디 -> sequential uuid 주의(@Column(columnDefinition = "BINARY(16)"))
     */
    @Builder(builderMethodName = "createAccount")
    public Account( String loginId, String name,String nickname, String password, String email, String contact, Authority authority) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.authority = authority;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String generateSequentialUUID(){
        return Generators.timeBasedGenerator().generate().toString();
    }

    /**
     * Authority 변경
     * 계정 수정일자 최신화
     * @param authority
     */
    public void updateAuthority(Authority authority){
        this.authority = authority;
        this.updatedAt = LocalDateTime.now();
    }

    public void setLogin(boolean isLogin){
        this.isLogin = isLogin;
    }


}
