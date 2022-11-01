package com.bmarket.securityservice.api.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESH_TOKEN_ID")
    private Long id;
    private String refreshToken;
    protected RefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public static RefreshToken createRefreshToken(String refreshToken) {
        return new RefreshToken(refreshToken);
    }
    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
