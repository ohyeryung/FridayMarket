package com.smile.fridaymarket_auth.domain.user.dto;

import com.smile.fridaymarket_auth.domain.user.entity.User;
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

    public User toEntity(User updateUser) {

        return User.builder()
                .username(updateUser.getUsername())
                .password(updateUser.getPassword())
                .phoneNumber(this.phoneNumber)
                .userRole(updateUser.getUserRole())
                .isDeleted(updateUser.getIsDeleted())
                .build();
    }

}
