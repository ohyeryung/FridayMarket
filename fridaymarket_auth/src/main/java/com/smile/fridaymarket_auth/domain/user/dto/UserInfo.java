package com.smile.fridaymarket_auth.domain.user.dto;

import com.smile.fridaymarket_auth.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {

    private String username;
    private String phoneNumber;

    public static UserInfo fromEntity(User user) {
        return UserInfo.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
