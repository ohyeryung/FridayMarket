package com.smile.fridaymarket_auth.domain.auth.token.repository;

import com.smile.fridaymarket_auth.domain.auth.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * RefreshToken을 저장합니다.
     *
     * @param refreshToken 저장할 RefreshToken
     */
    public void save(final RefreshToken refreshToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.refreshToken(), refreshToken.username());
        // 만기일 설정
        long TOKEN_EXPIRY_DAYS = 7L;
        redisTemplate.expire(refreshToken.refreshToken(), TOKEN_EXPIRY_DAYS, TimeUnit.DAYS); // TTL 설정
    }

    /**
     * RefreshToken을 통해 유저 정보를 찾습니다.
     *
     * @param refreshToken 유저 정보를 찾을 RefreshToken
     * @return RefreshToken과 관련된 유저 정보를 담은 Optional RefreshToken
     */
    public Optional<RefreshToken> findByUsername(final String refreshToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String username = valueOperations.get(refreshToken);

        if (Objects.isNull(username)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, username));
    }

    /**
     * 만료된 RefreshToken을 삭제합니다.
     *
     * @param now 현재 시간
     */
    public void deleteExpiredTokens(LocalDateTime now) {

    }

    /**
     * 수동으로 RefreshToken을 삭제합니다.
     *
     * @param refreshToken refreshToken
     */
    public void delete(RefreshToken refreshToken) {

        redisTemplate.delete(refreshToken.refreshToken());
    }

}

