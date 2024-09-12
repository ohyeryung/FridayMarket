package com.smile.fridaymarket_auth.grpc;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.domain.auth.JwtTokenProvider;
import com.smile.fridaymarket_auth.global.response.ErrorResponse;
import io.grpc.*;
import io.grpc.Metadata.Key;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtServerInterceptor implements ServerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    // Authorization 헤더 키 생성 (Metadata.Key)
    private static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        // Authorization 헤더에서 토큰을 가져옴
        String token = headers.get(AUTHORIZATION_KEY);
        log.info("0. gRPC 통신으로 헤더에서 받아온 token : {}", token);

        if (token == null) {
            // 토큰이 없으면 UNAUTHENTICATED 상태로 응답
            call.close(Status.UNAUTHENTICATED.withDescription("토큰이 없습니다."), headers);
            return new ServerCall.Listener<>() {}; // 빈 리스너 반환
        }

        // 토큰 유효성 검증
        Optional<DecodedJWT> decodedJWT = jwtTokenProvider.isValidToken(token);

        if (decodedJWT.isEmpty()) {
            // 만료된 토큰 등 유효하지 않은 경우, 커스텀 메시지와 함께 응답
            ErrorResponse errorResponse = new ErrorResponse(false, "만료된 토큰입니다.", "BAD_REQUEST");
            Metadata trailers = new Metadata();
            trailers.put(Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER), toJson(errorResponse));
            call.close(Status.UNAUTHENTICATED.withDescription("만료된 토큰입니다."), trailers);
            return new ServerCall.Listener<>() {}; // 빈 리스너 반환
        }

        // 검증 성공 시, 다음 핸들러 호출
        return next.startCall(call, headers);
    }

    /**
     * ErrorResponse를 JSON 형식으로 변환합니다.
     *
     * @param errorResponse 에러 응답 객체
     * @return JSON 형식의 문자열
     */
    private String toJson(ErrorResponse errorResponse) {
        try {
            return new ObjectMapper().writeValueAsString(errorResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"서버 오류\",\"httpStatus\":\"INTERNAL_SERVER_ERROR\"}";
        }
    }
}
