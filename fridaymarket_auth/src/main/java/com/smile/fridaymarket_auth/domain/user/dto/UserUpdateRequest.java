package com.smile.fridaymarket_auth.domain.user.dto;

import com.smile.fridaymarket_auth.domain.user.service.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @ValidPhoneNumber
    private String phoneNumber;

}
