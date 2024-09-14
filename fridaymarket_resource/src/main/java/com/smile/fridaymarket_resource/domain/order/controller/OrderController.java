package com.smile.fridaymarket_resource.domain.order.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.service.OrderService;
import com.smile.fridaymarket_resource.global.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public SuccessResponse<String> createOrder(HttpServletRequest request, @Valid @RequestBody OrderCreateRequest orderCreateRequest) {

        UserResponse user = getUser(request);
        orderService.createOrder(user.userId(), orderCreateRequest);
        return SuccessResponse.successWithNoData("주문이 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/isPaymentReceived", method = RequestMethod.PATCH)
    public SuccessResponse<String> isPaymentReceived(HttpServletRequest request, @PathVariable Long orderId) {

        UserResponse user = getUser(request);
        orderService.isPaymentReceived(user.userId(), orderId);
        return SuccessResponse.successWithNoData("입금 완료되었습니다.");
    }

}
