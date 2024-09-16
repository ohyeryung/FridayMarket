package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.dto.OrderProductRequest;
import com.smile.fridaymarket_resource.domain.order.entity.CancelRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.repository.CancelRequestRepository;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.service.PriceReader;
import com.smile.fridaymarket_resource.domain.product.entity.Product;
import com.smile.fridaymarket_resource.domain.product.service.ProductReader;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType.BUY;
import static com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType.SELL;
import static com.smile.fridaymarket_resource.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductReader productReader;
    private final PriceReader priceReader;
    private final OrderStore orderStore;
    private final OrderReader orderReader;
    private final CancelRequestRepository cancelRequestRepository;

    @Override
    @Transactional
    public void createOrder(String userId, OrderCreateRequest request) {

        // 주문서(OrderInvoice) 생성 및 저장
        OrderInvoice orderInvoice = orderStore.saveOrderInvoice(userId, request);

        BigDecimal totalAmount = BigDecimal.ZERO; // 총 금액 계산

        // 각 상품(OrderProduct)을 처리하고 저장
        for (OrderProductRequest productRequest : request.getOrderProducts()) {
            // 각 상품에 대해 Product 가져오기
            Product product = productReader.getProduct(productRequest.getProductId());

            // 기존 DB에서 해당 상품의 가격 가져오기
            Price price = priceReader.getPrice(productRequest.getPrice(), request.getOrderType(), userId);


            OrderProduct orderProduct = orderStore.saveOrderProduct(request, orderInvoice, product, price, productRequest);

            // 총 금액 합산
            totalAmount = totalAmount.add(orderProduct.getPrice().multiply(orderProduct.getQuantity()));
        }

        // 주문서 금액 업데이트
        orderInvoice.setAmount(totalAmount);
        orderStore.saveOrderInvoice(orderInvoice);
    }

    /**
     * 2. 주문 상태값 변경 (입금 완료)
     *
     * @param userId 유저 Id
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void isPaymentReceived(String userId, Long orderId) {

        // 1. 주문 체크
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 본인의 주문인지 체크
        verifyOrderOwnership(userId, orderInvoice);

        // 3. 주문 상태가 '주문 완료'이면서 '구매 주문'인지 확인
        if (!orderInvoice.getOrderStatus().equals(OrderStatus.ORDER_COMPLETE) || orderInvoice.getOrderType().equals(SELL)) {
            throw new CustomException(ORDER_STATUS_NOT_ORDER_COMPLETED);
        }

        // 3. 주문 완료 상태에서 결제가 완료되었다는 가정하에 자동으로 상태값 변경
        orderInvoice.updateStatusIsPaymentReceived();

    }

    /**
     * 주문의 송장 입력 받았다는 가정 하에 주문에 대한 상태값을 발송 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void isShipped(Long orderId) {

        // 1. 주문 존재 여부 확인
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 주문 상태가 '입금 완료'이면서 '구매 주문'인지 확인
        if (!orderInvoice.getOrderStatus().equals(OrderStatus.PAYMENT_RECEIVED) || orderInvoice.getOrderType().equals(SELL))
            throw new CustomException(ORDER_STATUS_NOT_PAYMENT_RECEIVED);

        // 3. 상품에 대한 송장 입력되었다는 가정 하에 상태 변경
        orderInvoice.updateStatusIsShipped();
    }

    /**
     * 관리자가 판매자가 보낸 상품을 수령완료 한 경우 주문에 대한 상태값을 수령 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void isReceived(Long orderId) {

        // 1. 주문 존재 여부 확인
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 주문 상태가 '주문 완료'이면서 '판매 주문'인지 확인
        if (!orderInvoice.getOrderStatus().equals(OrderStatus.ORDER_COMPLETE) || orderInvoice.getOrderType().equals(BUY))
            throw new CustomException(ORDER_STATUS_NOT_RECEIVED);

        // 3. 검수센터에서 상품 수령했다는 가정 하에 상태 변경
        orderInvoice.updateStatusIsReceived();
    }

    /**
     * 관리자가 판매자의 상품 검수 및 정상 상품의 경우 송금 완료 후 해당 주문의 상태값을 송금 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void isPaymentSent(Long orderId) {

        // 1. 주문 존재 여부 확인
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 주문 상태가 '수령 완료'이면서 '판매 주문'인지 확인
        if (!orderInvoice.getOrderStatus().equals(OrderStatus.RECEIVED) || orderInvoice.getOrderType().equals(BUY))
            throw new CustomException(ORDER_STATUS_NOT_PAYMENT_SENT);

        // 3. 상품에 대한 금액 송금 완료 후 상태 변경
        orderInvoice.updateStatusIsPaymentSent();
    }

    /**
     * 사용자가 주문 취소 요청을 보냄, 취소 요청 객체 생성
     *
     * @param userId 유저 Id
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void cancelRequestByUser(String userId, Long orderId) {

        // 1. 주문 존재 여부 확인
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 본인의 주문인지 검증
        verifyOrderOwnership(userId, orderInvoice);

        // 3. 주문 취소 요청 객체 생성 및 저장
        CancelRequest cancelRequest = CancelRequest.builder()
                .userId(UUID.fromString(userId))
                .orderId(orderId)
                .requestedAt(LocalDateTime.now())
                .isApproved(false)
                .build();

        cancelRequestRepository.save(cancelRequest);
    }

    /**
     * 관리자가 주문 취소 요청을 확인하고 실제로 주문을 삭제하지는 않고 상태값만 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

        // 1. 주문 존재 여부 검증
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 취소 요청 확인
        CancelRequest cancelRequest = cancelRequestRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException(CANCEL_REQUEST_NOT_FOUND)
        );

        // 3. 취소 요청에 대한 승인 완료 상태값 변경
        cancelRequest.updateStatus();

        // 4. 주문에 대한 상태값 취소 상태로 변경 및 softDelete 처리
        orderInvoice.updateStatusCanceled();

    }

    /**
     * 본인의 주문인지 검증
     *
     * @param userId 유저 Id
     * @param orderInvoice 주문 객체
     */
    private void verifyOrderOwnership(String userId, OrderInvoice orderInvoice) {

        if (!orderInvoice.getUserId().equals(UUID.fromString(userId))) {
            throw new CustomException(ILLEGAL_ORDER_USER_NOT_MATCHED);
        }
    }

}
