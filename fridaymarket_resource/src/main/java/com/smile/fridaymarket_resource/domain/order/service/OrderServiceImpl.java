package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.*;
import com.smile.fridaymarket_resource.domain.order.entity.CancelRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.repository.CancelRequestRepository;
import com.smile.fridaymarket_resource.domain.order.repository.OrderInvoiceRepository;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.service.PriceReader;
import com.smile.fridaymarket_resource.domain.product.entity.Product;
import com.smile.fridaymarket_resource.domain.product.service.ProductReader;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final OrderInvoiceRepository orderInvoiceRepository;

    /**
     * 1. 주문 등록
     *
     * @param userId  유저 Id
     * @param request 주문 타입, 상품 Id, 수량, g당 가격, 배송지 정보로 주문 등록 요청
     */
    @Override
    @Transactional
    public void createOrder(String userId, OrderCreateRequest request) {

        // 주문서(OrderInvoice) 생성 및 저장
        // 1-1. 사용자의 ID와 요청된 주문 정보를 바탕으로 새로운 주문서 객체를 생성하고 데이터베이스에 저장.
        OrderInvoice orderInvoice = orderStore.saveOrderInvoice(userId, request);

        // 총 금액 계산을 위한 변수 초기화
        // 1-2. 주문에 포함된 상품들의 총 금액을 계산하기 위해 초기값을 0으로 설정.
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 각 상품(OrderProduct)을 처리하고 저장
        // 1-3. 요청된 주문에 포함된 각 상품을 하나씩 처리하기 위한 반복문 시작.
        for (OrderProductRequest productRequest : request.getOrderProducts()) {

            // 각 상품에 대해 Product 가져오기
            // 1-4. productRequest에 담긴 productId를 이용하여 상품 정보를 조회.
            Product product = productReader.getProduct(productRequest.getProductId());

            // 기존 DB에서 해당 상품의 가격 가져오기
            // 1-5. DB에서 해당 상품에 대한 가격 정보를 가져옴. 기존 Price DB에 같은 주문 타입과 가격이 존재한다면 해당 데이터를 사용
            Price price = priceReader.getPrice(productRequest.getPrice(), request.getOrderType(), userId);

            // OrderProduct 객체 생성 및 저장
            // 1-6. 상품 정보, 주문서 정보, 가격 및 요청된 상품 수량 등을 바탕으로 주문 상품(OrderProduct) 객체를 생성하고 저장.
            OrderProduct orderProduct = orderStore.saveOrderProduct(request, orderInvoice, product, price, productRequest);

            // 총 금액 합산
            // 1-7. 상품의 가격과 수량을 곱한 값을 총 금액에 더함.
            totalAmount = totalAmount.add(orderProduct.getPrice().multiply(orderProduct.getQuantity()));
        }

        // 주문서 금액 업데이트
        // 1-8. 반복문을 통해 모든 상품의 총 금액을 계산한 후, 해당 금액을 주문서(OrderInvoice) 객체에 업데이트.
        orderInvoice.updateAmount(totalAmount);

        // 주문서 저장 (업데이트)
        // 1-9. 금액이 업데이트된 주문서를 다시 데이터베이스에 저장.
        orderStore.saveOrderInvoice(orderInvoice);
    }


    /**
     * 2. 주문 상태값 변경 (입금 완료)
     *
     * @param userId  유저 Id
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
     * 3. 주문의 송장 입력 받았다는 가정 하에 주문에 대한 상태값을 발송 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     *
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
     * 4. 관리자가 판매자가 보낸 상품을 수령완료 한 경우 주문에 대한 상태값을 수령 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     *
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
     * 5. 관리자가 판매자의 상품 검수 및 정상 상품의 경우 송금 완료 후 해당 주문의 상태값을 송금 완료로 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     *
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
     * 6. 사용자가 주문 취소 요청을 보냄, 취소 요청 객체 생성
     *
     * @param userId  유저 Id
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
     * 7. 관리자가 주문 취소 요청을 확인하고 실제로 주문을 삭제하지는 않고 상태값만 변경
     * TODO : 관리자만 가능, 권한 설정 필요
     *
     * @param orderId 주문 Id
     */
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

        // 1. 주문 존재 여부 검증
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 취소 요청 확인 및 상태 업데이트
        CancelRequest cancelRequest = cancelRequestRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException(CANCEL_REQUEST_NOT_FOUND));
        cancelRequest.updateStatus();

        // 3. 주문에 대한 상태값 취소 상태로 변경 및 softDelete 처리
        orderInvoice.updateStatusCanceled();

    }

    /**
     * 8. 주문 상세 조회
     *
     * @param userId  유저 Id
     * @param orderId 주문 Id
     * @return 조회 요청한 주문
     */
    @Override
    public OrderResponse getOrderInvoice(String userId, Long orderId) {

        // 1. 주문 존재 여부 조회
        OrderInvoice orderInvoice = orderReader.getOrderInvoice(orderId);

        // 2. 본인 주문인지 검증
        verifyOrderOwnership(userId, orderInvoice);

        // 3. 응답 객체로 변환
        return orderInvoice.toResponseDto(orderInvoice);

    }

    /**
     *
     * @param userId 유저 Id
     * @param pageable 페이징 처리 data
     * @return 페이징처리 된 주문 내역 목록 반환
     */
    @Override

    public OrderPaging getOrderList(String userId, Pageable pageable) {

        Page<OrderList> orderList = orderInvoiceRepository.getOrderList(userId, pageable);
        return new OrderPaging(orderList);
    }

    /**
     * 본인의 주문인지 검증
     *
     * @param userId       유저 Id
     * @param orderInvoice 주문 객체
     */
    private void verifyOrderOwnership(String userId, OrderInvoice orderInvoice) {

        if (!orderInvoice.getUserId().equals(UUID.fromString(userId))) {
            throw new CustomException(ILLEGAL_ORDER_USER_NOT_MATCHED);
        }
    }

}
