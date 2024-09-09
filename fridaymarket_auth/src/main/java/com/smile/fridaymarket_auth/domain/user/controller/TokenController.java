package com.smile.fridaymarket_auth.domain.user.controller;

import com.smile.fridaymarket_auth.domain.auth.token.service.RefreshTokenManager;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenManager refreshTokenManager;

    @Operation(summary = "accessToken 재발급", description = "accessToken 재발급 합니다.")
    @RequestMapping(value = "/api/refresh", method = RequestMethod.GET)
    public ResponseEntity<HttpHeaders> refreshAccessToken(@RequestHeader("RefreshToken") String refreshToken) {

        HttpHeaders headers = refreshTokenManager.refreshAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

}
