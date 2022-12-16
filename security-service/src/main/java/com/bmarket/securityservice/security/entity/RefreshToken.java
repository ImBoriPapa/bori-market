package com.bmarket.securityservice.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Refresh token
 */
@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESH_TOKEN_ID")
    private Long id;
    @Column(name = "REFRESH_TOKEN")
    private String token;
    protected RefreshToken(String token){
        this.token = token;
    }

    /**
     * Refresh token 생성 메서드
     * @param refreshToken
     * @return
     */
    public static RefreshToken createRefreshToken(String refreshToken) {
        return new RefreshToken(refreshToken);
    }

    /**
     * Refresh token 변경 메서드
     */
    public String changeRefreshToken(String refreshToken){
        return  this.token = refreshToken;
    }

    /**
     * 로그아웃시 토큰 삭제
     */
    public void deleteToken(){
        this.token = null;
    }
}
