package com.smile.fridaymarket_auth.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.domain.auth.UserDetailsImpl;
import com.smile.fridaymarket_auth.domain.auth.token.service.JwtTokenUtils;
import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserInfo;
import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.domain.user.entity.User;
import com.smile.fridaymarket_auth.domain.user.entity.UserRole;
import com.smile.fridaymarket_auth.domain.user.repository.UserRepository;
import com.smile.fridaymarket_auth.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserDetailsImpl mockUserDetails;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockitoAnnotations.openMocks(this);

        User mockUser = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(mockUser);

        // "testUser" 계정명을 가진 UserDetails Mock 객체 설정
        given(mockUserDetails.getUsername()).willReturn("testUser");

        // SecurityContext에 UserDetails Mock 객체 설정 => SecurityContext에 Authentication 객체를 설정하여, 이후 테스트에서 인증된 사용자의 정보를 사용할 수 있도록 함
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities()));

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("형식에 맞지 않는 아이디로 회원가입을 진행합니다.")
    void signupWithNotValidUsername() throws Exception {

        // given: 형식에 맞지 않는 아이디를 포함한 UserCreateRequest 객체를 생성합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("te")  // 3자리 미만의 아이디
                .password("testPassword!")
                .phoneNumber("01012345678")
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("형식에 맞지 않는 비밀번호로 회원가입을 진행합니다.")
    void signupWithNotValidPassword() throws Exception {

        // given: 형식에 맞지 않는 비밀번호를 포함한 UserCreateRequest 객체를 생성합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("short")  // 비밀번호가 너무 짧음
                .phoneNumber("01012345678")
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("형식에 맞지 않는 전화번호로 회원가입을 진행합니다.")
    void signupWithNotValidPhoneNumber() throws Exception {

        // given: 형식에 맞지 않는 전화번호를 포함한 UserCreateRequest 객체를 생성합니다.
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("testPassword!")
                .phoneNumber("invalidPhoneNumber")  // 유효하지 않은 전화번호
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("형식에 맞지 않는 아이디로 로그인 합니다.")
    void loginWithNotValidUsername() throws Exception {

        // given: 형식에 맞지 않는 아이디를 포함한 LoginRequest 객체를 생성합니다.
        LoginRequest request = LoginRequest.builder()
                .username("te")  // 3자리 미만의 아이디
                .password("testPassword!")
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("형식에 맞지 않는 비밀번호로 로그인 합니다.")
    void loginWithNotValidPassword() throws Exception {

        // given: 형식에 맞지 않는 비밀번호를 포함한 LoginRequest 객체를 생성합니다.
        LoginRequest request = LoginRequest.builder()
                .username("testUser")
                .password("short")  // 형식에 맞지 않는 비밀번호
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원정보 조회 해피 케이스 테스트를 진행합니다.")
    void getUserInfoWithSuccess() throws Exception {
        // given: 테스트에 사용할 유저를 생성 및 저장합니다.
        User user = User.builder()
                .username("testUser")
                .password(passwordEncoder.encode("testPassword!"))
                .phoneNumber("01012345678")
                .userRole(Set.of(UserRole.NORMAL))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        UserInfo userInfo = UserInfo.builder()
                .username("testUser")
                .phoneNumber("01012345678")
                .build();

        // Mock 서비스 응답
        given(userService.getUserInfo("testUser")).willReturn(userInfo);

        // given: 임시 AccessToken을 생성합니다.
        String token = "Bearer " + generateTokenForUser(user);


        // when & then: HTTP GET 요청을 수행하고, 상태 코드가 200(OK)인지 확인하며, 응답의 data 필드에 유저 정보가 포함되어 있는지 검증합니다.
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true)) // 응답의 success 필드 확인
                .andExpect(jsonPath("$.message").value("OK")) // 응답의 message 필드 확인
                .andExpect(jsonPath("$.data.username").value(user.getUsername())) // data 안의 username 확인
                .andExpect(jsonPath("$.data.phoneNumber").value(user.getPhoneNumber())); // data 안의 phoneNumber 확인

    }

    // AccessToken 생성
    private String generateTokenForUser(User user) {

        return jwtTokenUtils.generateAccessToken(user.getUsername());
    }

    @Test
    @DisplayName("형식에 맞지 않는 전화번호로 회원 정보를 수정합니다.")
    void updateUserInfoWithNotValidPhoneNumber() throws Exception {

        // given: 형식에 맞지 않는 전화번호를 포함한 UserCreateRequest 객체를 생성합니다.
        UserUpdateRequest request = UserUpdateRequest.builder()
                .phoneNumber("invalidPhoneNumber")  // 유효하지 않은 전화번호
                .build();

        // when & then: HTTP POST 요청을 수행하고, 상태 코드가 400(BadRequest)인지 확인합니다.
        mockMvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false)) // 응답의 success 필드 확인
                .andExpect(jsonPath("$.message").value("유효하지 않은 전화번호입니다.")); // 응답의 message 필드 확인
    }

}
