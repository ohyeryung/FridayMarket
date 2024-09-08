package com.smile.fridaymarket_auth.domain.user.dto;

import com.smile.fridaymarket_auth.domain.user.service.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @ValidPassword
    private String password;

}
