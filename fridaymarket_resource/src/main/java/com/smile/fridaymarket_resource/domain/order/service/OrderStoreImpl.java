package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.order.repository.OrderInvoiceRepository;
import com.smile.fridaymarket_resource.domain.order.repository.OrderProductRepository;
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
     * @param userId  유저 Id
     * @param request 주문 타입, 상품 Id, g당 금액, 수량, 배송지
     * @param product 상품
     * @param price   g당 가격
     * @return 저장된 주문 객체 반환
     */
    @Override
    @Transactional
    public OrderInvoice saveOrderInvoice(String userId, OrderCreateRequest request, Product product, BigDecimal price) {

        OrderInvoice orderInvoice = OrderInvoice.builder()
                .userId(UUID.fromString(userId))
                .orderType(request.getOrderType())
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .product(product)
                .amount(getTotalPrice(request.getQuantity(), price))
                .deliveryAddress(request.getDeliveryAddress())
                .build();

        // 주문 번호 생성 및 설정
        String orderNo = generateOrderNo(orderInvoice.getId(), orderInvoice.getOrderType());
        orderInvoice.setOrderNo(orderNo);

        return orderInvoiceRepository.save(orderInvoice);
    }

    /**
     * 주문별 주문 타입과 상품의 가격 및 수량을 저장
     *
     * @param request      주문 타입, 상품 Id, g당 금액, 수량, 배송지
     * @param orderInvoice 주문 객체
     * @param product      상품
     * @param price        g당 가격
     */
    @Override
    @Transactional
    public void saveOrderProduct(OrderCreateRequest request, OrderInvoice orderInvoice, Product product, BigDecimal price) {

        OrderProduct orderProduct = OrderProduct.builder()
                .orderType(request.getOrderType())
                .orderInvoice(orderInvoice)
                .product(product)
                .price(price)
                .quantity(request.getQuantity())
                .build();

        orderProductRepository.save(orderProduct);
    }

    /**
     * 주문 총액 계산
     *
     * @param quantity  수량 (g단위)
     * @param unitPrice g당 가격
     * @return 계산된 총액 반환
     */
    private BigDecimal getTotalPrice(BigDecimal quantity, BigDecimal unitPrice) {

        return unitPrice.multiply(quantity);
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
