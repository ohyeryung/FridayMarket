package com.smile.fridaymarket_auth.grpc.service;

import com.smile.fridaymarket_auth.domain.auth.JwtTokenProvider;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.service.UserReader;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import com.smile.fridaymarket_auth.grpc.AuthTokenProto;
import com.smile.fridaymarket_auth.grpc.AuthTokenServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GrpcAuthService extends AuthTokenServiceGrpc.AuthTokenServiceImplBase {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserReader userReader;

    @Override
    public void verifyToken(AuthTokenProto.AuthTokenRequest request, StreamObserver<AuthTokenProto.AuthTokenResponse> responseObserver) {

        String accessToken = request.getAccessToken();

        AuthTokenProto.AuthTokenResponse.Builder responseBuilder = AuthTokenProto.AuthTokenResponse.newBuilder();

        String username = jwtTokenProvider.getUsernameFromToken(accessToken).replace("\"", "");

        try {

            User user = userReader.getUserByUsername(username);

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
