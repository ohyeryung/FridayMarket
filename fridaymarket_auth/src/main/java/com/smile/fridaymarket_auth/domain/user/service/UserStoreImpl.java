package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.entity.UserRole;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public User store(User initUser) {

        User user = User.builder()
                .username(initUser.getUsername())
                .password(passwordEncoder.encode(initUser.getPassword()))
                .phoneNumber(initUser.getPhoneNumber())
                // 구매 주문 또는 판매 주문 작성 전에는 일반 권한을 기본 값으로 설정
                .userRole(Set.of(UserRole.NORMAL))
                // 사용자 탈퇴 여부는 false를 기본 값으로 설정
                .isDeleted(false)
                .build();

        return userRepository.save(user);
    }

}
