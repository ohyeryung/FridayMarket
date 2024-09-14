package com.smile.fridaymarket_auth.grpc.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.domain.auth.JwtTokenProvider;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import com.smile.fridaymarket_auth.global.exception.CustomJwtException;
import com.smile.fridaymarket_auth.global.exception.ErrorCode;
import com.smile.fridaymarket_auth.global.response.ErrorResponse;
import io.grpc.*;
import io.grpc.Metadata.Key;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@GrpcGlobalServerInterceptor
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

        if (token == null || token.isEmpty()) {
            throw new CustomException(ErrorCode.ILLEGAL_ACCESS_TOKEN_NOT_VALID);
        }

        // 'Bearer ' 접두사 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 뒤의 부분만 남김
        }

        try {
            // 토큰 유효성 검증
            Optional<DecodedJWT> decodedJWT = jwtTokenProvider.isValidToken(token);

            if (decodedJWT.isEmpty()) {
                return closeCallWithError(call, headers, Status.UNAUTHENTICATED, "유효하지 않은 토큰입니다.");
            }
        } catch (CustomJwtException e) {
            return closeCallWithError(call, headers, Status.UNAUTHENTICATED, e.getMessage());
        }

        // 검증 성공 시, 다음 핸들러 호출
        return next.startCall(call, headers);
    }

    /**
     * 인증 실패 시 gRPC 호출을 종료하고 에러 메시지를 반환합니다.
     *
     * @param call      ServerCall 객체
     * @param headers   gRPC 헤더
     * @param status    반환할 gRPC 상태 코드
     * @param errorDesc 에러 설명
     * @return 빈 ServerCall.Listener 객체
     */
    private <ReqT, RespT> ServerCall.Listener<ReqT> closeCallWithError(ServerCall<ReqT, RespT> call, Metadata headers, Status status, String errorDesc) {

        log.warn("gRPC 호출 실패: {}", errorDesc);
        ErrorResponse errorResponse = new ErrorResponse(false, errorDesc, "BAD_REQUEST");
        Metadata trailers = new Metadata();
        trailers.put(Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER), toJson(errorResponse));
        call.close(status.withDescription(errorDesc), trailers);
        return new ServerCall.Listener<>() {
        };  // 빈 리스너 반환
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
            log.error("JSON 변환 오류: {}", e.getMessage());
            return "{\"success\":false,\"message\":\"서버 오류\",\"httpStatus\":\"INTERNAL_SERVER_ERROR\"}";
        }
    }

}
