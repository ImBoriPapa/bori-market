package com.bmarket.securityservice.domain.account.entity;

import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.domain.security.entity.RefreshToken;
import com.bmarket.securityservice.exception.custom_exception.security_ex.IsLogoutAccountException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import com.fasterxml.uuid.Generators;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id", "clientId"})
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
    @Column(unique = true, name = "EMAIL")
    private String email;
    @Column(unique = true)
    private String contact;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "AUTHORITY")
    private Authority authority;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)//일대일 단방향 주 테이블 Account
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
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
     *
     * @param loginId  로그안 아이디
     * @param name     사용자 이름
     * @param password 비밀번호
     * @param contact
     * @param email
     * @param profile  clientId 사용자 식별 아이디 -> sequential uuid 주의(@Column(columnDefinition = "BINARY(16)"))
     *                 최초 가입시 Authority =ROLL_USER
     */
    @Builder(builderMethodName = "createAccount")
    public Account(String loginId, String name, String password, String email, String contact, Profile profile) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.authority = Authority.USER;
        this.profile = profile;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Account createAdmin(String loginId, String name, String password, String email, String contact,Authority authority) {
        return new Account(loginId, name, password, email, contact,authority);
    }

    private Account(String loginId, String name, String password, String email, String contact,Authority authority) {
        this.clientId = generateSequentialUUID();
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.authority = authority;
        this.isLogin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String updateClientId() {
        this.updatedAt = LocalDateTime.now();
        return this.clientId = generateSequentialUUID();

    }

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
     * 계정 수정일자 최신화
     *
     * @param authority
     */
    public void updateAuthority(Authority authority) {
        this.authority = authority;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 로그인
     */
    public void loginIn() {
        this.isLogin = true;
        this.lastLoginTime = LocalDateTime.now();
    }

    /**
     * 1.로그아웃 요청시 리프레시 토큰 삭제
     * 2.clientId 변경
     * 이미 로그아웃된 계정이라면 IsLogoutAccountException
     */
    public void logout() {
        if (!this.isLogin) {
            throw new IsLogoutAccountException(ResponseStatus.THIS_ACCOUNT_IS_LOGOUT);
        }
        this.isLogin = false;
        this.getRefreshToken().deleteToken();
        this.clientId = generateSequentialUUID();
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

    public Collection<? extends GrantedAuthority> getAuthorityList() {
        ArrayList<SimpleGrantedAuthority> objects = new ArrayList<>();
        Arrays.asList(this.authority.ROLL.split(","))
                .forEach(m -> objects.add(new SimpleGrantedAuthority(m)));
        return objects;
    }

    public static Collection<? extends GrantedAuthority> getAuthorityList(Authority authority) {
        ArrayList<SimpleGrantedAuthority> objects = new ArrayList<>();
        Arrays.asList(authority.ROLL.split(","))
                .forEach(m -> objects.add(new SimpleGrantedAuthority(m)));
        return objects;
    }

    public void addRefresh(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }


}
