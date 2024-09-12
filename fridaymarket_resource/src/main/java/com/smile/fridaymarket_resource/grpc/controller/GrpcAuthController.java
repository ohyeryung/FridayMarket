package com.smile.fridaymarket_resource.grpc.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.grpc.service.GrpcAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class GrpcAuthController {

    private final GrpcAuthService grpcAuthService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserResponse authToken(@RequestHeader(name = "Authorization") String accessToken) {

        // 'Bearer ' 접두사 제거
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        // 인증 서버로 gRPC 호출을 통해 토큰 검증 요청
        return grpcAuthService.authToken(accessToken);

    }

}
