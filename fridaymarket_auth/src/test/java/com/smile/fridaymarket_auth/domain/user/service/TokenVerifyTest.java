package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.entity.UserRole;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class TokenVerifyTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 해피 케이스 테스트입니다.")
    void loginWithSuccess() {

        // given : 새 유저 객체 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .username("testUser")
                .password("testPassword!")
                .build();

        // when : 가입된 유저 정보로 로그인을 시도합니다.
        HttpHeaders httpHeaders = userService.login(loginRequest);

        // then : 헤더에 토큰이 발급됩니다.
        assertNotNull(httpHeaders);

        // 헤더에서 AccessToken 확인
        String accessToken = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("Bearer "));

        // 헤더에서 RefreshToken 확인
        String refreshToken = httpHeaders.getFirst("RefreshToken");
        assertNotNull(refreshToken);

    }

}
