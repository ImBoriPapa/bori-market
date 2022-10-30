package com.bmarket.securityservice.api.account.entity;

import com.bmarket.securityservice.api.profile.entity.Profile;
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
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Authority authority;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
    private boolean isLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginTime;

    /**
     * 계정 생성
     *
     * @param loginId  로그안 아이디
     * @param name     사용자 이름
     * @param password 비밀번호
     *                 clientId 사용자 식별 아이디 -> sequential uuid 주의(@Column(columnDefinition = "BINARY(16)"))
     *                 최초 가입시 Authority =ROLL_USER
     */
    @Builder(builderMethodName = "createAccount")
    public Account(String loginId, String name, String password, Profile profile) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.authority = Authority.ROLL_USER;
        this.profile = profile;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    public void updateClientId() {
        this.clientId = generateSequentialUUID();
    }

    private String generateSequentialUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    /**
     * Authority 변경
     * 계정 수정일자 최신화
     *
     * @param authority
     */
    public void updateAuthority(Authority authority) {
        this.authority = authority;
        this.updatedAt = LocalDateTime.now();
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }



}
