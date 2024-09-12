package com.smile.fridaymarket_auth.domain.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // header에서 token을 받아온다
        String token = jwtTokenProvider.resolveToken(request);
        log.info("token : {}", token);

        String requestURI = request.getRequestURI();
        log.debug("doFilter 들어옴");

        if (token != null) {
            // Redis에서 해당 accessToken이 로그아웃 처리된 것인지 확인
            String isLogout = redisTemplate.opsForValue().get(token);
            log.info("isLogout : {}", isLogout);

            // 로그아웃된 토큰일 경우 검증 실패 처리
            if (!ObjectUtils.isEmpty(isLogout)) {
                log.info("해당 토큰은 로그아웃된 토큰입니다.");
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다. 재로그인 후 이용해주세요.");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                response.getWriter().flush();
                return;
            }

            // 로그아웃이 아닌 경우 토큰을 검증하고 SecurityContext에 인증 정보 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }

        log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        filterChain.doFilter(request, response);
    }

}
