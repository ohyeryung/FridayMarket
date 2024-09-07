package com.smile.fridaymarket_auth.domain.auth;

import com.smile.fridaymarket_auth.domain.auth.token.service.JwtTokenUtils;
import com.smile.fridaymarket_auth.domain.auth.token.service.RefreshTokenManager;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthImpl implements UserAuth {

    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenManager refreshTokenManager;

    /**
     * Jwt AccessToken을 생성합니다.
     *
     * @param user 유저 객체
     * @return 생성된 accessToken
     */
    @Override
    public String getAccessToken(User user) {

        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername());
        log.info("유저 아이디 : {}, 생성된 AccessToken : {}", user.getUsername(), accessToken);
        return accessToken;
    }

    /**
     * RefreshToken을 생성하고 Redis에 저장합니다.
     *
     * @param user 유저 객체
     * @return 생성된 refreshToken
     */
    @Override
    public String saveRefreshTokenToRedis(User user) {

        String refreshToken = refreshTokenManager.saveJwtRefreshToken(user.getUsername());
        log.info("유저 아이디 : {}, 생성 및 저장된 RefreshToken : {}", user.getUsername(), refreshToken);
        return refreshToken;
    }

    /**
     * 응답 헤더에 accessToken과 refreshToken을 담아 반환합니다.
     *
     * @param user 유저 객체
     * @return accessToken과 refreshToken이 담긴 헤더
     */
    @Override
    public HttpHeaders generateHeaderTokens(User user) {

        HttpHeaders headers = new HttpHeaders();
        String accessToken = getAccessToken(user);
        String refreshToken = saveRefreshTokenToRedis(user);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.set("RefreshToken", refreshToken);
        log.info("AccessToken 및 RefreshToken 생성완료 후 헤더에 추가 완료");
        log.info("해당 유저 아이디 : {}", user.getUsername());
        return headers;
    }

}
