package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.auth.UserAuth;
import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserInfo;
import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final UserAuth userAuth;

    /**
     * 1. 회원 가입
     *
     * @param userCreateRequest 아이디, 비밀번호, 전화번호로 회원가입 진행
     */
    @Override
    @Transactional
    public void signup(UserCreateRequest userCreateRequest) {

        // 사용자 아이디 중복 체크
        userReader.checkUsernameDuplicate(userCreateRequest.getUsername());
        // 사용자 객체 생성 및 저장
        userStore.store(userCreateRequest.createUser());

    }

    /**
     * 2. 로그인
     *
     * @param loginRequest 아이디, 비밀번호로 로그인 진행
     * @return 생성된 AccessToken과 RefreshToken을 담은 HttpHeaders
     */
    @Override
    @Transactional
    public HttpHeaders login(LoginRequest loginRequest) {

        // 유저 검증 및 객체 조회
        User user = userReader.getUser(loginRequest.getUsername(), loginRequest.getPassword());
        // 헤더에 Access 토큰 및 Refresh 토큰 생성 후 전달
        return userAuth.generateHeaderTokens(user);

    }

    /**
     * 3. 회원정보 조회
     *
     * @param username 유저 아이디
     * @return 해당 유저의 아이디와 전화번호가 담긴 UserInfo 반환
     */
    @Override
    @Transactional
    public UserInfo getUserInfo(String username) {

        // 유저 아이디로 유저 검증 및 객체 조회
        User userByUsername = userReader.getUserByUsername(username);

        return UserInfo.fromEntity(userByUsername);
    }

    /**
     * 4. 회원정보 수정
     *
     * @param username 유저 아이디
     * @param updateRequest 변경하려는 전화번호
     * @return 변경된 전화번호와 해당 유저의 아이디 값을 반환
     */
    @Override
    @Transactional
    public UserInfo updateUserInfo(String username, UserUpdateRequest updateRequest) {
        // 유저 아이디로 유저 검증 및 객체 조회
        User updateUser = userReader.getUserByUsername(username);

        // 유효성 검사 통과한 전화번호만 업데이트
        updateUser.updatePhoneNumber(updateRequest.getPhoneNumber());

        // 유저 정보 반환 (자동으로 영속성 컨텍스트가 변경 사항을 감지하여 DB에 반영)
        return UserInfo.fromEntity(updateUser);
    }

}
