package com.smile.fridaymarket_auth.domain.auth.token.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${ACCESS_TOKEN_VALID_MILLI_SEC}")
    private int ACCESS_TOKEN_VALID_MILLI_SEC;

    @Value("${REFRESH_TOKEN_VALID_MILLI_SEC}")
    private int REFRESH_TOKEN_VALID_MILLI_SEC;

    @Value("${CLAIM_USERNAME}")
    private String claimUsername; // 인스턴스 필드

    public static String CLAIM_NAME; // static 필드

    @PostConstruct
    public void init() {
        CLAIM_NAME = claimUsername; // 주입된 값을 static 필드에 할당
    }

    @Value("${JWT_ISSUER}")
    private String JWT_ISSUER;

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET;

    /**
     * AccessToken을 생성합니다.
     *
     * @param username 유저 아이디
     * @return 생성된 AccessToken
     */
    public String generateAccessToken(String username) {

        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim("type", "access")
                .withClaim(CLAIM_NAME, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm(JWT_SECRET));
    }

    /**
     * RefreshToken을 생성합니다.
     *
     * @param username 유저 아이디
     * @return 생성된 RefreshToken
     */
    public String generateRefreshToken(String username) {

        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim("type", "refresh")
                .withClaim(CLAIM_NAME, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm(JWT_SECRET));
    }

    /**
     * 토큰 서명을 위한 알고리즘을 생성합니다.
     *
     * @param secretKey 비밀키
     * @return HMAC256 알고리즘
     */
    private Algorithm generateAlgorithm(String secretKey) {

        return Algorithm.HMAC256(secretKey.getBytes());
    }

}
