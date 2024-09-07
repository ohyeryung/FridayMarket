package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.smile.fridaymarket_auth.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디 중복체크
     *
     * @param username 아이디
     */
    @Override
    public void checkUsernameDuplicate(String username) {

        userRepository.findByUsername(username).ifPresent(
                user -> {
                    throw new CustomException(ILLEGAL_USERNAME_DUPLICATION);
                }
        );
    }

    /**
     * 유저 객체 검증
     *
     * @param username 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 해당 유저 객체 반환
     */
    @Override
    public User getUser(String username, String password) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ILLEGAL_USER_NOT_EXIST)
        );
        // 입력된 비밀번호, 기존 비밀번호 순서대로 입력해야 비교가 가능
        isValidPassword(password, user.getPassword());
        return user;
    }

    /**
     * 비밀번호 확인
     *
     * @param comparePassword 새로 입력한 비밀번호
     * @param originPassword  기존 비밀번호
     */
    private void isValidPassword(String comparePassword, String originPassword) {

        if (!passwordEncoder.matches(comparePassword, originPassword)) {
            throw new CustomException(ILLEGAL_PASSWORD_NOT_VALID);
        }
    }

}
