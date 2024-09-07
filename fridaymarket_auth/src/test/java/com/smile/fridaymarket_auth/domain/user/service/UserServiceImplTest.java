package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.entity.UserRole;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import com.smile.fridaymarket_auth.global.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {

        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입 해피 케이스 테스트입니다.")
    void signup() {
        // given: UserCreateRequest 객체를 생성합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("testPassword")
                .phoneNumber("01012345678")
                .build();


        // when: signup 메서드를 호출합니다.
        userService.signup(request);

        // then: 사용자 정보가 데이터베이스에 저장되었는지 확인합니다.
        User savedUser = userRepository.findByUsername("testUser")
                .orElseThrow(() -> new CustomException(ErrorCode.ILLEGAL_USER_NOT_EXIST));

        assertThat(savedUser.getUsername()).isEqualTo("testUser");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(savedUser.getUserRole()).containsExactly(UserRole.NORMAL);

    }

}
