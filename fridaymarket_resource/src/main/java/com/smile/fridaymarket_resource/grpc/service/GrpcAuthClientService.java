package com.smile.fridaymarket_resource.grpc.service;

import com.smile.fridaymarket_auth.grpc.AuthTokenProto;
import com.smile.fridaymarket_auth.grpc.AuthTokenServiceGrpc;
import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.grpc.interceptor.JwtClientInterceptor;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrpcAuthClientService {

    @GrpcClient("auth")
    private AuthTokenServiceGrpc.AuthTokenServiceBlockingStub authStub;

    public UserResponse authToken(String accessToken) {

        try {
            // 인터셉터를 추가한 Stub 생성 (주입된 authStub을 사용)
            AuthTokenServiceGrpc.AuthTokenServiceBlockingStub interceptedStub =
                    authStub.withInterceptors(new JwtClientInterceptor(accessToken));

            AuthTokenProto.AuthTokenResponse authTokenResponse = interceptedStub.verifyToken(
                    AuthTokenProto.AuthTokenRequest.newBuilder()
                            .setAccessToken(accessToken)
                            .build()
            );

            log.info("gRPC 통신 응답 username: {}", authTokenResponse.getUsername());

            return new UserResponse(true, authTokenResponse.getUserId(), authTokenResponse.getUsername());
        }
        catch (StatusRuntimeException e) {
            log.info("gRPC 호출 실패 : {}", e.getStatus().getCode().name());
            return new UserResponse(false, "Unknown", "Unknown");
        }
    }
}
