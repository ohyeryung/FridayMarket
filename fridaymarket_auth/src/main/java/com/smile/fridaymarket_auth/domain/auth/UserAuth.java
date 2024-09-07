package com.smile.fridaymarket_auth.domain.auth;

import com.smile.fridaymarket_auth.domain.user.entity.User;
import org.springframework.http.HttpHeaders;

public interface UserAuth {

    String getAccessToken(User user);

    String saveRefreshTokenToRedis(User user);

    HttpHeaders generateHeaderTokens(User user);

}
