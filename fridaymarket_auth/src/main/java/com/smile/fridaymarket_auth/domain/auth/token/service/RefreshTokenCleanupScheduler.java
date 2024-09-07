package com.smile.fridaymarket_auth.domain.auth.token.service;

import com.smile.fridaymarket_auth.domain.auth.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 주기적으로 만료된 RefreshToken을 삭제합니다.
     * 매일 자정에 실행되도록 설정합니다.
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void cleanUpExpiredTokens() {

        LocalDateTime now = LocalDateTime.now();
        // 만료된 토큰을 찾고 삭제합니다.
        refreshTokenRepository.deleteExpiredTokens(now);
    }

}
