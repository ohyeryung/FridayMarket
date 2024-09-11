package com.smile.fridaymarket_resource.grpc.service;

import com.smile.fridaymarket_auth.grpc.AuthTokenProto;
import com.smile.fridaymarket_auth.grpc.AuthTokenServiceGrpc;
import com.smile.fridaymarket_resource.auth.UserResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrpcAuthService {

    @GrpcClient("auth")
    private AuthTokenServiceGrpc.AuthTokenServiceBlockingStub authStub;

    public UserResponse authToken(String accessToken) {
        try {
            AuthTokenProto.AuthTokenResponse authTokenResponse = authStub.verifyToken(
                    AuthTokenProto.AuthTokenRequest.newBuilder()
                            .setAccessToken(accessToken)
                            .build()
            );
            log.info("gRPC 통신 응답 username: {}", authTokenResponse.getUsername());

            return new UserResponse(true, authTokenResponse.getUserId(), authTokenResponse.getUsername());
        }
        catch (StatusRuntimeException e) {
            // gRPC 호출 실패 시 오류 메시지 기록
            log.info("gRPC 호출 실패 : {}", e.getStatus().getCode().name());
            // 인증 실패 응답 반환
            return new UserResponse(false, "Unknown", "Unknown");
        }
    }
}
