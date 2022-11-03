package com.bmarket.securityservice.api.account.entity;

import com.bmarket.securityservice.api.profile.entity.Profile;
import com.bmarket.securityservice.api.security.entity.RefreshToken;
import com.fasterxml.uuid.Generators;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id","clientId"})
@Table(name = "ACCOUNT",indexes = @Index(name = "idx__name",columnList = "EMAIL"))
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;
    @Column(unique = true, name = "CLIENT_ID")
    private String clientId;
    @Column(unique = true, name = "LOGIN_ID")
    private String loginId;
    @Column(name = "USER_NAME")
    private String name;
    @Column(name = "PASSWORD")
    private String password;
    @Column(unique = true,name = "EMAIL")
    private String email;
    @Column(unique = true)
    private String contact;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "AUTHORITY")
    private Authority authority;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)//일대일 단방향 주 테이블 Account
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)//일대일 단방향 주 테이블 Account
    @JoinColumn(name = "REFRESH_TOKEN")
    private RefreshToken refreshToken;
    @Column(name = "IS_LOGIN")
    private boolean isLogin;
    @Column(name = "CREATED_AT",updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginTime;

    /**
     * 계정 생성
     * @param loginId  로그안 아이디
     * @param name     사용자 이름
     * @param password 비밀번호
     * @param contact
     * @param email
     * @param profile
     *        clientId 사용자 식별 아이디 -> sequential uuid 주의(@Column(columnDefinition = "BINARY(16)"))
     *        최초 가입시 Authority =ROLL_USER
     */
    @Builder(builderMethodName = "createAccount")
    public Account(String loginId, String name, String password, String email, String contact, Profile profile) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.authority = Authority.ROLL_USER;
        this.profile = profile;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Account(String loginId, String name, String password, String email, String contact) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.authority = Authority.ROLL_ADMIN;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateClientId() {
        this.clientId = generateSequentialUUID();
        this.updatedAt = LocalDateTime.now();
    }
    private  String generateSequentialUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    /**
     * Authority 변경
     * 계정 수정일자 최신화
     * @param authority
     */
    public void updateAuthority(Authority authority) {
        this.authority = authority;
        this.updatedAt = LocalDateTime.now();
    }

    public void loginCheck(boolean isLogin) {
        this.isLogin = isLogin;
        if(this.isLogin){
            this.lastLoginTime = LocalDateTime.now();
        }
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContact(String contact) {
        this.contact = contact;
        this.updatedAt = LocalDateTime.now();
    }
}
