package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.dto.OrderProductRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.order.repository.OrderInvoiceRepository;
import com.smile.fridaymarket_resource.domain.order.repository.OrderProductRepository;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderStoreImpl implements OrderStore {

    private final OrderInvoiceRepository orderInvoiceRepository;

    private final OrderProductRepository orderProductRepository;

    /**
     * 주문 저장
     *
     * @param userId 유저 Id
     * @param request 주문 타입, 상품, 수량, 금액, 배송지 데이터로 요청
     * @return 저장된 주문 객체 반환
     */
    @Override
    @Transactional
    public OrderInvoice saveOrderInvoice(String userId, OrderCreateRequest request) {

        // 주문서(OrderInvoice) 생성
        OrderInvoice orderInvoice = OrderInvoice.builder()
                .userId(UUID.fromString(userId))
                .orderType(request.getOrderType())
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .amount(BigDecimal.ZERO) // 초기 금액 설정
                .deliveryAddress(request.getDeliveryAddress())
                .build();

        // 주문서 저장
        orderInvoiceRepository.save(orderInvoice);

        // 주문 번호 생성 및 설정
        String orderNo = generateOrderNo(orderInvoice.getId(), orderInvoice.getOrderType());

        orderInvoice.setOrderNo(orderNo);

        // 주문서 업데이트
        return orderInvoiceRepository.save(orderInvoice);
    }

    /**
     * 이미 생성된 주문에 총 금액이 계산된 결과 값 포함된 상태의 객체 저장
     *
     * @param orderInvoice 저장하려는 주문 객체
     */
    @Override
    public void saveOrderInvoice(OrderInvoice orderInvoice) {
        orderInvoiceRepository.save(orderInvoice);
    }

    @Override
    @Transactional
    public OrderProduct saveOrderProduct(OrderCreateRequest request, OrderInvoice orderInvoice, Product product, Price price, OrderProductRequest productRequest) {

        // OrderProduct 생성 및 저장
        OrderProduct orderProduct = OrderProduct.builder()
                .orderType(request.getOrderType())
                .orderInvoice(orderInvoice)
                .product(product)
                .price(price.getPrice()) // 가격 설정
                .quantity(productRequest.getQuantity())
                .build();

        return orderProductRepository.save(orderProduct);
    }

    /**
     * 주문번호 생성
     *
     * @param orderId   주문 Id
     * @param orderType 주문 타입
     * @return 생성된 주문번호 반환 (ex. SELL-0910-0001)
     */
    private String generateOrderNo(Long orderId, OrderType orderType) {

        // 주문 타입 (BUY, SELL) + 날짜(월일) + 주문 ID
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
        return orderType.name() + "-" + date + "-" + String.format("%04d", orderId); // 주문 ID는 4자리 형식
    }

}
