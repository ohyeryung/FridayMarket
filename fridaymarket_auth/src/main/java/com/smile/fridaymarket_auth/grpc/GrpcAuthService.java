package com.smile.fridaymarket_auth.grpc;

import com.smile.fridaymarket_auth.domain.auth.JwtTokenProvider;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.service.UserReader;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@GrpcService
public class GrpcAuthService extends AuthTokenServiceGrpc.AuthTokenServiceImplBase {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserReader userReader;

    public GrpcAuthService(JwtTokenProvider jwtTokenProvider, UserReader userReader) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.userReader = userReader;
    }
    
    @Override
    @Transactional
    public void verifyToken(AuthTokenProto.AuthTokenRequest request, StreamObserver<AuthTokenProto.AuthTokenResponse> responseObserver) {

        String accessToken = request.getAccessToken();

        AuthTokenProto.AuthTokenResponse.Builder responseBuilder = AuthTokenProto.AuthTokenResponse.newBuilder();

        String username = jwtTokenProvider.getUsernameFromToken(accessToken).replace("\"", "");
        log.info("1. gRPC통신으로 찾은 username : {}", username);

        try {
            // 로그 추가
            User user = userReader.getUserByUsername(username);
            log.info("2. DB에서 찾은 사용자: {}", user.getUsername());

            UUID userId = user.getId();

            responseBuilder
                    .setUserId(String.valueOf(userId))
                    .setUsername(username);

            responseObserver.onNext(responseBuilder.build());
        } catch (CustomException e) {
            log.error("사용자 조회 중 예외 발생: {}", e.getMessage());
            responseBuilder.setSuccess(false);
            responseObserver.onNext(responseBuilder.build());
        }

        responseObserver.onCompleted();
    }

}
