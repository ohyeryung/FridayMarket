package com.smile.fridaymarket_auth.domain.auth.token.service;

import com.smile.fridaymarket_auth.domain.auth.token.entity.RefreshToken;
import com.smile.fridaymarket_auth.domain.auth.token.repository.RefreshTokenRepository;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.smile.fridaymarket_auth.global.exception.ErrorCode.ILLEGAL_REFRESH_TOKEN_NOT_VALID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    @Transactional
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

    /**
     * RefreshToken의 유효성을 검증합니다.
     *
     * @param refreshToken jwt 형태의 refreshToken
     * @return 검증이 완료된 refreshToken 객체 반환
     */
    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByUsername(refreshToken).orElseThrow(
                () -> new CustomException(ILLEGAL_REFRESH_TOKEN_NOT_VALID)
        );

    }

    /**
     * RefreshToken의 유효성을 검증합니다.
     *
     * @param refreshToken 검증할 RefreshToken
     * @return 유저 아이디 반환, 유효하지 않은 경우 null
     */
    public String validateRefreshToken(String refreshToken) {

        Optional<RefreshToken> token = refreshTokenRepository.findByUsername(refreshToken);
        return token.map(RefreshToken::username).orElse(null);
    }

    /**
     * RefreshToken을 검증하고 새로운 AccessToken을 생성합니다.
     *
     * @param refreshToken 검증할 RefreshToken
     * @return 새로 생성된 AccessToken을 담은 HttpHeaders
     */
    public HttpHeaders refreshAccessToken(String refreshToken) {

        String username = validateRefreshToken(refreshToken);

        if (username == null) {
            throw new IllegalArgumentException("유효하지 않은 RefreshToken 입니다.");
        }

        String newAccessToken = jwtTokenUtils.generateAccessToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer " + newAccessToken);
        return headers;
    }


}
