package com.bmarket.securityservice.account.domain.entity;

import com.bmarket.securityservice.security.entity.RefreshToken;
import com.fasterxml.uuid.Generators;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;
    @Column(unique = true, name = "MEMBER_ID")
    private String memberId;
    @Column(unique = true, name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "MEMBER_NAME")
    private String name;
    @Column(name = "NICK_NAME")
    private String nickName;
    @Column(unique = true)
    private String contact;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "AUTHORITY")
    private Authority authority;
    @Embedded
    private Address address;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)//일대일 단방향 주 테이블 Account
    @JoinColumn(name = "REFRESH_TOKEN")
    private RefreshToken refreshToken;
    @Column(name = "IS_LOGIN")
    private boolean isLogin;
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginTime;
    @Column(name = "LAST_LOGOUT_AT")
    private LocalDateTime lastLogoutTime;

    /**
     * 계정 생성
     */
    @Builder(builderMethodName = "createAccount")
    public Account(String email, String password, String name, String nickName, String contact, Authority authority, Address address) {
        this.memberId = generateSequentialUUID();
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.contact = contact;
        this.authority = authority;
        this.address = address;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 시간순 정렬 UUID
     */
    private String generateSequentialUUID() {
        String proto = Generators.timeBasedGenerator().generate().toString();
        String[] array = proto.split("-");
        String sort = array[2] + array[1] + array[0] + array[3] + array[4];
        StringBuilder builder = new StringBuilder(sort);
        builder.insert(8, "-");
        builder.insert(13, "-");
        builder.insert(18, "-");
        builder.insert(23, "-");
        return builder.toString();
    }

    /**
     * Authority 변경
     */
    public void updateAuthority(Authority authority) {
        this.authority = authority;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 로그인 상태
     */
    public void login() {
        this.isLogin = true;
        this.lastLoginTime = LocalDateTime.now();
    }

    /**
     * 1.로그아웃 요청시 리프레시 토큰 삭제
     * 2.clientId 변경
     */
    public void logout() {
        this.isLogin = false;
        this.getRefreshToken().deleteToken();
        this.lastLogoutTime = LocalDateTime.now();
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

    public void addRefresh(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }


}
