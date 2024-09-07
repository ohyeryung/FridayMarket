package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    @Transactional
    public void signup(UserCreateRequest userCreateRequest) {

        // 사용자 아이디 중복 체크
        userReader.checkUsernameDuplicate(userCreateRequest.getUsername());
        // 사용자 객체 생성 및 저장
        userStore.store(userCreateRequest.createUser());

    }


}
