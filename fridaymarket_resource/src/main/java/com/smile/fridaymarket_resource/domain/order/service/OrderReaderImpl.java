package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.repository.OrderInvoiceRepository;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.smile.fridaymarket_resource.global.exception.ErrorCode.ORDER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class OrderReaderImpl implements OrderReader {

    private final OrderInvoiceRepository orderInvoiceRepository;

    /**
     * 주문 조회
     *
     * @param orderId 주문 Id
     * @return 조회된 주문 객체 반환
     */
    @Override
    public OrderInvoice getOrderInvoice(Long orderId) {

        return orderInvoiceRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ORDER_NOT_FOUND)
        );
    }

}
