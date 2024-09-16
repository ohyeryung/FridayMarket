package com.smile.fridaymarket_resource.domain.price.service;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PriceReaderImpl implements PriceReader {

    private final PriceRepository priceRepository;
    private final PriceStore priceStore;

    /**
     * 가격 조회 (DB에서 주문 타입과 가격이 같은 경우에는 기존 가격을 사용, 없다면 새로 저장)
     *
     * @param price     g당 가격
     * @param orderType 주문 타입
     * @param userId    유저 Id
     * @return 조회된 가격 객체 반환
     */
    @Override
    public Price getPrice(BigDecimal price, OrderType orderType, String userId) {

        return priceRepository.findByPriceAndOrderType(price, orderType)
                .orElseGet(() -> priceStore.saveNewPrice(price, orderType, userId));
    }

}
