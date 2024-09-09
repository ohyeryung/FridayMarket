package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserInfo;
import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.global.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void signup(@Valid UserCreateRequest userCreateRequest);

    HttpHeaders login(LoginRequest loginRequest);

    UserInfo getUserInfo(String username);

    UserInfo updateUserInfo(String username, UserUpdateRequest updateRequest);

    String logout(String username, String accessToken, String refreshToken);

}
