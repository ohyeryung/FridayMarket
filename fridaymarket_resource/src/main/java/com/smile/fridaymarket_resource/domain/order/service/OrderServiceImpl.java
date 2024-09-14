package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.service.PriceReader;
import com.smile.fridaymarket_resource.domain.product.entity.Product;
import com.smile.fridaymarket_resource.domain.product.service.ProductReader;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.smile.fridaymarket_resource.global.exception.ErrorCode.ILLEGAL_ORDER_USER_NOT_MATCHED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductReader productReader;
    private final PriceReader priceReader;
    private final OrderStore orderStore;
    private final OrderReader orderReader;

    /**
     * 1. 주문 등록
     *
     * @param userId 유저 Id
     * @param request 주문 타입, 상품 Id, g당 금액, 수량, 배송지로 주문 등록 요청
     */
    @Override
    @Transactional
    public void createOrder(String userId, OrderCreateRequest request) {

        // 상품 가져오기
        Product product = productReader.getProduct(request.getProductId());

        // 가격 가져오기 (DB에서 주문 타입과 가격이 같은 경우에는 기존 가격 사용)
        Price price = priceReader.getPrice(request.getPrice(), request.getOrderType(), userId);

        // 주문 생성 및 저장
        OrderInvoice orderInvoice = orderStore.saveOrderInvoice(userId, request, product, price.getPrice());

        // OrderProduct 생성 및 저장
        orderStore.saveOrderProduct(request, orderInvoice, product, price.getPrice());
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

        // 3. 주문 완료 상태에서 결제가 완료되었다는 가정하에 자동으로 상태값 변경
        orderInvoice.updateStatusIsPaymentReceived();

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
