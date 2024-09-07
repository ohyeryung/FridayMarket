package com.smile.fridaymarket_auth.domain.auth.token.service;

import com.smile.fridaymarket_auth.domain.auth.token.entity.RefreshToken;
import com.smile.fridaymarket_auth.domain.auth.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * JWT RefreshToken을 생성하고 저장합니다.
     *
     * @param username 유저 아이디
     * @return 저장된 RefreshToken
     */
    public String saveJwtRefreshToken(final String username) {

        String refreshToken = jwtTokenUtils.generateRefreshToken(username);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsername(username);

        if (existingToken.isPresent()) {
            log.warn("이미 존재하는 RefreshToken 해당 유저아이디 : {} 기존의 RefreshToken 토큰은 무효화", username);
            refreshTokenRepository.delete(existingToken.get());
        }

        refreshTokenRepository.save(new RefreshToken(refreshToken, username));
        return refreshToken;
    }

}
