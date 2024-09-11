package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.order.repository.OrderInvoiceRepository;
import com.smile.fridaymarket_resource.domain.order.repository.OrderProductRepository;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.repository.PriceRepository;
import com.smile.fridaymarket_resource.domain.product.entity.Product;
import com.smile.fridaymarket_resource.domain.product.entity.ProductRepository;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import com.smile.fridaymarket_resource.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderInvoiceRepository orderRepository;
    private final PriceRepository priceRepository;

    private final OrderProductRepository orderProductRepository;

    @Override
    @Transactional
    public void createOrder(String userId, OrderCreateRequest request) {
        log.info("request.getProductId() : {}", request.getProductId());

        // 상품 가져오기
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 새로 등록된 가격 저장
        Price price = Price.builder()
                .price(request.getPrice())
                .orderType(request.getOrderType())
                .createdBy(UUID.fromString(userId))
                .build();

        priceRepository.save(price);

        // 주문 생성
        OrderInvoice orderInvoice = OrderInvoice.builder()
                .userId(UUID.fromString(userId))
                .orderType(request.getOrderType())
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .product(product)
                .amount(getTotalPrice(request.getQuantity(), request.getPrice()))
                .deliveryAddress(request.getDeliveryAddress())
                .build();

        // 주문 번호 생성 및 설정
        String orderNo = generateOrderNo(orderInvoice.getId(), orderInvoice.getOrderType());
        log.info("orderNo : {}", orderNo);

        orderInvoice.setOrderNo(orderNo);

        // 주문 저장
        orderInvoice = orderRepository.save(orderInvoice);

        // OrderProduct 생성 및 저장
        OrderProduct orderProduct = OrderProduct.builder()
                .orderType(request.getOrderType())
                .orderInvoice(orderInvoice)
                .product(product)
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        // OrderProduct 저장
        orderProductRepository.save(orderProduct);
    }

    @Override
    public void isPaymentReceived(String userId, Long orderId) {

    }

    // 주문 총액 계산
    private BigDecimal getTotalPrice(BigDecimal quantity, BigDecimal unitPrice) {
        return unitPrice.multiply(quantity);
    }

    // 주문 번호 생성
    private String generateOrderNo(Long orderId, OrderType orderType) {
        // 주문 타입 (BUY, SELL) + 날짜(월일) + 주문 ID
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
        return orderType.name() + "-" + date + "-" + String.format("%04d", orderId); // 주문 ID는 4자리 형식
    }
}
