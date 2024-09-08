package com.smile.fridaymarket_auth.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.fridaymarket_auth.domain.user.dto.LoginRequest;
import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import com.smile.fridaymarket_auth.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("형식에 맞지 않는 아이디로 회원가입을 진행합니다.")
    void signupWithNotValidUsername() throws Exception {

        UserCreateRequest request = UserCreateRequest.builder()
                .username("te")  // 형식에 맞지 않는 아이디
                .password("testPassword!")
                .phoneNumber("01012345678").build();

        mockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("형식에 맞지 않는 비밀번호로 회원가입을 진행합니다.")
    void signupWithNotValidPassword() throws Exception {

        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("short")  // 형식에 맞지 않는 비밀번호
                .phoneNumber("01012345678").build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("형식에 맞지 않는 전화번호로 회원가입을 진행합니다.")
    void signupWithNotValidPhoneNumber() throws Exception {

        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password("testPassword!")
                .phoneNumber("invalidPhoneNumber")  // 형식에 맞지 않는 전화번호
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("형식에 맞지 않는 아이디로 로그인 합니다.")
    void loginWithNotValidUsername() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .username("te")  // 형식에 맞지 않는 아이디
                .password("testPassword!")
                .build();

        mockMvc
                .perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("형식에 맞지 않는 비밀번호로 로그인 합니다.")
    void loginWithNotValidPassword() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .username("testUser")
                .password("short") // 형식에 맞지 않는 비밀번호
                .build();

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 예외가 발생해야 합니다.
    }
}
