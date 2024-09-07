package com.smile.fridaymarket_auth.domain.user.controller;

import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.service.UserService;
import com.smile.fridaymarket_auth.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public SuccessResponse<String> signup(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        userService.signup(userCreateRequest);
        return SuccessResponse.successWithNoData("회원가입 성공");
    }


}
