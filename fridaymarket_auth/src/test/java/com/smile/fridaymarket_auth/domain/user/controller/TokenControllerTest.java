package com.smile.fridaymarket_auth.domain.user.controller;

import com.smile.fridaymarket_auth.domain.auth.token.service.RefreshTokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshTokenManager refreshTokenManager;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("RefreshToken으로 AccessToken을 재발급합니다.")
    void refreshAccessToken() throws Exception {
        // given
        String mockRefreshToken = "mockRefreshToken"; // 실제 JWT가 아닌 모킹된 토큰
        String newAccessToken = "mockAccessToken"; // Mock에서 설정된 값 사용

        // Mock 메서드 응답 설정
        when(refreshTokenManager.refreshAccessToken(mockRefreshToken)).thenReturn(new HttpHeaders() {{
            set(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
        }});

        // when & then
        mockMvc.perform(get("/api/refresh")
                        .header("RefreshToken", mockRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken));
    }
}
