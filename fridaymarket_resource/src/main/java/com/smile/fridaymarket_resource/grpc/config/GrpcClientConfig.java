package com.smile.fridaymarket_resource.grpc.config;

import com.smile.fridaymarket_auth.grpc.AuthTokenServiceGrpc;
import com.smile.fridaymarket_resource.grpc.filter.JwtClientInterceptor;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;

public class GrpcClientConfig {

    @Value("${GRPC_HOST}")
    private static String grpcHost;

    @Value("${GRPC_AUTH_SERVER_PORT}")
    private static int grpcServerPort;

    public static ManagedChannel createChannel() {

        return ManagedChannelBuilder
                .forAddress(grpcHost, grpcServerPort)
                .usePlaintext()
                .build();
    }

    public static AuthTokenServiceGrpc.AuthTokenServiceBlockingStub createStub(String accessToken) {

        // JWT 인터셉터 추가
        JwtClientInterceptor jwtInterceptor = new JwtClientInterceptor(accessToken);
        Channel interceptedChannel = ClientInterceptors.intercept(createChannel(), jwtInterceptor);

        // gRPC 서비스 스텁 생성
        return AuthTokenServiceGrpc.newBlockingStub(interceptedChannel);
    }
}
