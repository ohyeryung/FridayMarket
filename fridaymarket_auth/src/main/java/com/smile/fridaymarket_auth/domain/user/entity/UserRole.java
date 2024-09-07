package com.smile.fridaymarket_auth.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    NORMAL("일반 권한"),
    BUYER("구매자 권한"),
    SELLER("판매자 권한"),
    ADMIN("관리자 권한"),
    SUPER_ADMIN("최종 관리자 권한");

    private final String displayName;
}
