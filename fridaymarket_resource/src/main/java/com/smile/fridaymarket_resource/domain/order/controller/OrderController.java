package com.smile.fridaymarket_resource.domain.order.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.service.OrderService;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import com.smile.fridaymarket_resource.global.exception.ErrorCode;
import com.smile.fridaymarket_resource.global.response.SuccessResponse;
import com.smile.fridaymarket_resource.grpc.service.GrpcAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final GrpcAuthService grpcAuthService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public SuccessResponse<String> createOrder(@RequestHeader("Authorization") String accessToken, @Valid @RequestBody OrderCreateRequest request) {

        UserResponse user = validateUser(accessToken);

        orderService.createOrder(user.userId(), request);
        return SuccessResponse.successWithNoData("주문이 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/isPaymentReceived", method = RequestMethod.PATCH)
    public SuccessResponse isPaymentReceived(@RequestHeader("Authorization") String accessToken, @PathVariable Long orderId) {
        UserResponse user = validateUser(accessToken);
        orderService.isPaymentReceived(user.userId(), orderId);
        return SuccessResponse.successWithNoData("입금 완료되었습니다.");
    }

    private UserResponse validateUser(String accessToken) {

        UserResponse user = grpcAuthService.authToken(accessToken);
        if (!user.success()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        return user;
    }
}
