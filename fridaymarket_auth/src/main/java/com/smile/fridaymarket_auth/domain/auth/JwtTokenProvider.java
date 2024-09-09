package com.smile.fridaymarket_auth.domain.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.domain.auth.token.service.JwtTokenUtils;
import com.smile.fridaymarket_auth.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET;

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsServiceImpl userDetailsService;

    private static final String TOKEN_PREFIX = "Bearer "; // JWT 토큰의 접두사

    /**
     * HTTP 요청에서 JWT AccessToken 을 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 반환
     */
    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        log.info("token : {}", bearerToken);

        // Authorization 헤더가 존재하고, "Bearer "로 시작하는 경우 토큰을 추출
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * JWT AccessToken 을 기반으로 인증 정보를 생성합니다.
     *
     * @param accessToken    JWT AccessToken
     * @param response HTTP 응답 객체
     * @return Authentication 객체 반환
     * @throws IOException 예외 발생 시
     */
    public Authentication getAuthentication(String accessToken, HttpServletResponse response) throws IOException {

        // 토큰에서 사용자 정보를 추출하여 UserDetails를 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodeUsername(accessToken, response));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT AccessToken 에서 사용자 계정명을 추출합니다.
     *
     * @param accessToken    JWT AccessToken
     * @param response HTTP 응답 객체
     * @return 사용자 계정명 반환
     * @throws IOException 예외 발생 시
     */
    public String decodeUsername(String accessToken, HttpServletResponse response) throws IOException {

        // 토큰의 유효성을 검사하고, 유효한 경우 사용자 계정명을 추출
        DecodedJWT decodedJWT = isValidToken(accessToken, response)
                .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));

        log.info("decodedJWT.getClaim() : {}", decodedJWT.getClaim(JwtTokenUtils.CLAIM_NAME));
        return decodedJWT
                .getClaim(JwtTokenUtils.CLAIM_NAME)
                .asString();
    }

    /**
     * JWT 토큰의 유효성을 검사합니다.
     *
     * @param token    JWT 토큰
     * @param response HTTP 응답 객체
     * @return 유효한 토큰을 DecodedJWT로 반환, 유효하지 않으면 Optional.empty()
     * @throws IOException 예외 발생 시
     */
    public Optional<DecodedJWT> isValidToken(String token, HttpServletResponse response) throws IOException {

        DecodedJWT jwt = null;
        try {
            // JWT 토큰 검증을 위한 JWTVerifier 생성
            JWTVerifier verifier = JWT
                    .require(generateAlgorithm(JWT_SECRET))
                    .build();
            jwt = verifier.verify(token);
        } catch (TokenExpiredException e) {
            // JWT 토큰 만료된 경우 에러 처리
            logger.error(e.getMessage());
            tokenExpired(response);
        }
        return Optional.ofNullable(jwt);
    }

    /**
     * 만료된 JWT 토큰 에 대한 에러 응답을 생성합니다.
     *
     * @param response HTTP 응답 객체
     * @throws IOException 예외 발생 시
     */
    public void tokenExpired(HttpServletResponse response) throws IOException {

        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

    /**
     * accessToken 의 만료시간 초기화
     *
     * @param accessToken accessToken
     * @return 해당 토큰의 초기화된 만료시간 반환
     */
    public Long getExpiration(String accessToken) {
        // accessToken이 'Bearer '로 시작하면 이를 제거
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        DecodedJWT jwt;
        JWTVerifier verifier = JWT
                .require(generateAlgorithm(JWT_SECRET))
                .build();
        jwt = verifier.verify(accessToken);
        // accessToken 남은 시간
        Date expiration = jwt.getExpiresAt();
        log.info("accessToken 남은 시간 : {}", expiration);

        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    /**
     * HMAC256 알고리즘을 생성합니다.
     *
     * @param secretKey JWT 비밀 키
     * @return HMAC256 알고리즘
     */
    private static Algorithm generateAlgorithm(String secretKey) {

        return Algorithm.HMAC256(secretKey.getBytes());
    }

}
