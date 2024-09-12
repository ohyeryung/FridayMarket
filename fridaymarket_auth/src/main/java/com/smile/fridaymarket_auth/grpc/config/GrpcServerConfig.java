package com.smile.fridaymarket_auth.grpc.config;

import com.smile.fridaymarket_auth.domain.auth.JwtTokenProvider;
import com.smile.fridaymarket_auth.domain.user.service.UserReader;
import com.smile.fridaymarket_auth.grpc.GrpcAuthService;
import com.smile.fridaymarket_auth.grpc.JwtServerInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Value("${SERVER_PORT}")
    private int serverPort;

    @Bean
    public Server grpcServer(JwtTokenProvider jwtTokenProvider, UserReader userReader) {
        return ServerBuilder.forPort(serverPort)
                .addService(new GrpcAuthService(jwtTokenProvider, userReader))
                .intercept(new JwtServerInterceptor(jwtTokenProvider))
                .build();
    }
}
