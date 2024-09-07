package com.smile.fridaymarket_auth.domain.user.controller;

import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.service.UserService;
import com.smile.fridaymarket_auth.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        return ResponseEntity
                .status(HttpStatus.CREATED) // 응답 상태 코드 설정
                .body(response); // 본문에 SuccessResponse 포함
    }


}
