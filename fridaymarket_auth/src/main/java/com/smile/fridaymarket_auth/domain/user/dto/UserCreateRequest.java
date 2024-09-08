package com.smile.fridaymarket_auth.domain.user.dto;

import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.service.validation.ValidPassword;
import com.smile.fridaymarket_auth.domain.user.service.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {

    @NotBlank(message = "아이디는 필수 값입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3자리 이상 20자리 이하로 설정해야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @ValidPassword
    private String password;

    @NotBlank(message = "전화번호는 필수 값입니다.")
    @ValidPhoneNumber
    private String phoneNumber;

    public User createUser() {
        return User.builder()
                .username(username)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }

}
