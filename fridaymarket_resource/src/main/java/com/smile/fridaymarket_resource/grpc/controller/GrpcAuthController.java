package com.smile.fridaymarket_resource.grpc.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import com.smile.fridaymarket_resource.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class GrpcAuthController { // gRPC 통신 테스트를 위한 클래스

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserResponse authToken(HttpServletRequest request) {
        // JwtInterceptor에서 이미 유저 정보를 검증하고 저장했음
        UserResponse user = (UserResponse) request.getAttribute("user");

        if (user == null || !user.success()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }

        return user; // 이미 검증된 사용자 정보 반환
    }
}
