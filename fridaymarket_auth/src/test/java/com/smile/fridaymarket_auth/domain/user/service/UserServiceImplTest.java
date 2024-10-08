package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserInfo;
import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.entity.UserRole;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import com.smile.fridaymarket_auth.global.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
// 테스트가 끝난 후 컨텍스트를 정리하여 데이터베이스 상태가 다른 테스트에 영향을 미치지 않도록 함.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {

        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입 해피 케이스 테스트입니다.")
    void signupWithSuccess() {
        // given: UserCreateRequest 객체를 생성합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("testPassword!")
                .phoneNumber("01012345678")
                .build();

        // when: 회원가입을 시도합니다.
        userService.signup(request);

        // then: 사용자 정보가 데이터베이스에 저장되었는지 확인합니다.
        User savedUser = userRepository.findByUsername("testUser")
                .orElseThrow(() -> new CustomException(ErrorCode.ILLEGAL_USER_NOT_EXIST));

        assertThat(savedUser.getUsername()).isEqualTo("testUser");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(savedUser.getUserRole()).containsExactly(UserRole.NORMAL);

    }

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입을 진행합니다.")
    void signupWithExistUsername() {
        // given : 새 유저 객체 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        // when : 기존의 유저 아이디로 회원가입을 시도합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("testPassword~")
                .phoneNumber("01087654321")
                .build();

        // then : 이미 가입된 아이디라는 예외가 발생합니다.
        CustomException exception = assertThrows(CustomException.class, () -> userService.signup(request));
        assertEquals(ErrorCode.ILLEGAL_USERNAME_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("가입되지 않은 아이디로 로그인 합니다.")
    void loginWithNotExistUsername() {
        // given : 가입되지 않은 로그인 request 생성합니다.
        LoginRequest loginRequest = LoginRequest.builder()
                .username("notExistUser")
                .password("testPassword!")
                .build();

        // when && then : 존재하지 않는 정보로 로그인 시도 시 해당 에러 메세지와 함께 예외가 발생합니다.
        CustomException exception = assertThrows(CustomException.class, () -> userService.login(loginRequest));
        assertEquals(ErrorCode.ILLEGAL_USER_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("DB의 비밀번호와 일치하지 않은 비밀번호로 로그인 합니다.")
    void loginWithNotValidPassword() {
        // given : 새 유저 객체 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .username("testUser")
                .password("newPassword")
                .build();

        // when && then : DB의 비밀번호와 일치하지 않은 비밀번호로 로그인 시도 시 해당 에러 메세지와 함께 예외가 발생합니다.
        CustomException exception = assertThrows(CustomException.class, () -> userService.login(loginRequest));
        assertEquals(ErrorCode.ILLEGAL_PASSWORD_NOT_VALID, exception.getErrorCode());
    }

    @Test
    @DisplayName("가입되지 않은 유저의 정보를 조회합니다.")
    void getUserInfoWithNotExistUser() {
        // given: 테스트에 사용할 유저를 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        // when && then : 가입되지 않은 유저 아이디로 회원 정보 조회 시도 시 해당 에러 메세지와 함께 예외가 발생합니다.
        CustomException exception = assertThrows(CustomException.class, () -> userService.getUserInfo("NotExistUser"));
        assertEquals(ErrorCode.ILLEGAL_USER_NOT_EXIST, exception.getErrorCode());

    }

    @Test
    @DisplayName("회원정보 수정 해피 케이스 테스트입니다.")
    void updateUserInfoWithSuccess() {
        // given: 테스트에 사용할 유저를 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        // 변경할 전화번호를 요청 응답으로 받아옵니다.
        UserUpdateRequest request = UserUpdateRequest.builder()
                .phoneNumber("01087654321")
                .build();

        // when : 회원 정보를 수정합니다.
        UserInfo userInfo = userService.updateUserInfo(user.getUsername(), request);

        // then : 바뀐 정보를 확인합니다.
        assertEquals("01087654321", userInfo.getPhoneNumber());

        // DB에서 다시 유저를 조회하여 변경 사항이 반영되었는지 확인합니다.
        User updatedUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        // DB에 저장된 유저의 전화번호가 업데이트된 번호와 일치하는지 확인합니다.
        assertEquals("01087654321", updatedUser.getPhoneNumber());
    }

    @Test
    @DisplayName("존재하지 않는 유저로 회원정보를 수정합니다.")
    void updateUserInfoWithNonExistentUser() {
        // given: 존재하지 않는 유저 아이디로 정보 수정을 시도합니다.
        String nonExistentUsername = "nonExistentUser";
        UserUpdateRequest request = UserUpdateRequest.builder()
                .phoneNumber("01087654321")
                .build();

        // when & then: DB에 유저가 존재하지 않을 때 400 에러코드와 해당 에러 메세지와 함께 예외가 발생합니다.
        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUserInfo(nonExistentUsername, request));
        assertEquals(ErrorCode.ILLEGAL_USER_NOT_EXIST, exception.getErrorCode());

    }

}
