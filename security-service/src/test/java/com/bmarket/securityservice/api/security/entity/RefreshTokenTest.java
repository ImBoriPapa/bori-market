package com.bmarket.securityservice.api.security.entity;

import com.bmarket.securityservice.domain.security.entity.RefreshToken;
import com.bmarket.securityservice.domain.security.repository.RefreshTokenRepository;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class RefreshTokenTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Test
    @DisplayName("리프레쉬 토큰 생성 테스트")
    void createRefreshToken() throws Exception{
        //given
        Long id = 1L;
        String generatedRefreshToken = jwtUtils.generateRefreshToken(id);
        RefreshToken refreshToken = RefreshToken.createRefreshToken(generatedRefreshToken);
        //when
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        RefreshToken findRefresh = refreshTokenRepository.findById(savedRefreshToken.getId())
                .orElseThrow(()->new IllegalArgumentException("리프레쉬 토큰을 찾을수 없습니다."));
        //then
        assertThat(findRefresh.getId()).isEqualTo(savedRefreshToken.getId());
        assertThat(findRefresh.getToken()).isEqualTo(savedRefreshToken.getToken());
    }

    @Test
    @DisplayName("리프레쉬 토큰 수정 테스트")
    void updateRefreshTokenTest() throws Exception{
        //given
        Long id1 = 1L;
        Long id2 = 2L;
        String token1 = jwtUtils.generateRefreshToken(id1);
        String token2 = jwtUtils.generateRefreshToken(id2);
        RefreshToken refreshToken = RefreshToken.createRefreshToken(token1);

        //when
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        RefreshToken findBefore = refreshTokenRepository.findById(savedToken.getId())
                .orElseThrow(()->new IllegalArgumentException("토큰을 찾을수 없습니다."));

        String beforeToken = findBefore.getToken();
        findBefore.changeRefreshToken(token2);

        RefreshToken findAfter = refreshTokenRepository.findById(savedToken.getId())
                .orElseThrow(()->new IllegalArgumentException("토큰을 찾을수 없습니다."));
        //then
        assertThat(beforeToken).isNotEqualTo(findAfter.getToken());
        assertThat(findAfter.getToken()).isEqualTo(token2);

    }



}