package com.bmarket.securityservice.api.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientId;
    private String refreshToken;

    protected RefreshToken(String clientId,String refreshToken){
        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }

    public static RefreshToken createRefreshToken(String clientId,String refreshToken) {
        return new RefreshToken(clientId,refreshToken);
    }

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
