package com.smile.fridaymarket_resource.domain.order.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseController {
    protected UserResponse getUser(HttpServletRequest request) {

        return (UserResponse) request.getAttribute("user");
    }

}
