package com.smile.fridaymarket_resource.domain.order.controller;

import com.smile.fridaymarket_resource.auth.UserResponse;
import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.dto.OrderResponse;
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

    @RequestMapping(value = "/{orderId}/isShipped", method = RequestMethod.PATCH)
    public SuccessResponse<String> isShipped(@PathVariable Long orderId) {
        orderService.isShipped(orderId);
        return SuccessResponse.successWithNoData("발송 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/isReceived", method = RequestMethod.PATCH)
    public SuccessResponse<String> isReceived(@PathVariable Long orderId) {
        orderService.isReceived(orderId);
        return SuccessResponse.successWithNoData("수령 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/isPaymentSent", method = RequestMethod.PATCH)
    public SuccessResponse<String> isPaymentSent(@PathVariable Long orderId) {
        orderService.isPaymentSent(orderId);
        return SuccessResponse.successWithNoData("송금 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/request/cancel", method = RequestMethod.POST)
    public SuccessResponse<String> cancelRequestByUser(HttpServletRequest request, @PathVariable Long orderId) {
        UserResponse user = getUser(request);
        orderService.cancelRequestByUser(user.userId(), orderId);
        return SuccessResponse.successWithNoData("주문 취소 요청 완료되었습니다.");
    }

    @RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.PATCH)
    public SuccessResponse<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return SuccessResponse.successWithNoData("주문 취소 되었습니다.");
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public SuccessResponse<OrderResponse> getOrderInvoice(HttpServletRequest request, @PathVariable Long orderId) {
        UserResponse user = getUser(request);
        return SuccessResponse.successWithData(orderService.getOrderInvoice(user.userId(), orderId));
    }
}
