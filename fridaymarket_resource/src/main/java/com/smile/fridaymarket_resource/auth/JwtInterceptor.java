package com.smile.fridaymarket_resource.auth;

import com.smile.fridaymarket_resource.global.exception.CustomException;
import com.smile.fridaymarket_resource.global.exception.ErrorCode;
import com.smile.fridaymarket_resource.grpc.service.GrpcAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final GrpcAuthService grpcAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }

        // 'Bearer ' 접두사 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // JWT 검증 로직 추가
        UserResponse user = validateToken(token);
        if (!user.success()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }

        // 유저 정보를 Request에 저장 (추후 사용 가능하도록)
        request.setAttribute("user", user);
        return true;
    }

    private UserResponse validateToken(String accessToken) {

        UserResponse user = grpcAuthService.authToken(accessToken);
        if (!user.success()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        return user;
    }
}
