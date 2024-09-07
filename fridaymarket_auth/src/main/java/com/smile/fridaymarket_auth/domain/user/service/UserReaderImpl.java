package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import com.smile.fridaymarket_auth.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.smile.fridaymarket_auth.global.exception.ErrorCode.ILLEGAL_USERNAME_DUPLICATION;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    /**
     * 아이디 중복체크
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

}
