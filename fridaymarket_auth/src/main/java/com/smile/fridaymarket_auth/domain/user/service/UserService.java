package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.dto.UserCreateRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void signup(@Valid UserCreateRequest userCreateRequest);

}
