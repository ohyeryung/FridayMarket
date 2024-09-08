package com.smile.fridaymarket_auth.domain.user.controller;

import com.smile.fridaymarket_auth.domain.auth.UserDetailsImpl;
import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserInfo;
import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.domain.user.service.UserService;
import com.smile.fridaymarket_auth.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> signup(@Valid @RequestBody UserCreateRequest userCreateRequest) {

        userService.signup(userCreateRequest);
        SuccessResponse<String> response = SuccessResponse.successWithNoData("CREATED");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {

        HttpHeaders httpHeaders = userService.login(loginRequest);
        SuccessResponse<String> response = SuccessResponse.successWithNoData("OK");
        return ResponseEntity.status(200).headers(httpHeaders).body(response);
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보 조회")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public SuccessResponse<UserInfo> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return SuccessResponse.successWithData(userService.getUserInfo(userDetails.getUsername()));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public SuccessResponse<UserInfo> updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @Valid @RequestBody UserUpdateRequest updateRequest) {

        return SuccessResponse.successWithData(userService.updateUserInfo(userDetails.getUsername(), updateRequest));
    }

}
