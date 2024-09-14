package com.smile.fridaymarket_resource.domain.price.service;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.price.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PriceStoreImpl implements PriceStore {
    private final PriceRepository priceRepository;

    /**
     * 가격 저장
     * @param price g당 가격
     * @param orderType 주문 타입
     * @param userId 유저 Id
     * @return 저장된 가격 객체 반환
     */
    @Override
    @Transactional
    public Price saveNewPrice(BigDecimal price, OrderType orderType, String userId) {

        Price newPrice = Price.builder()
                .price(price)
                .orderType(orderType)
                .createdBy(UUID.fromString(userId))
                .build();
        return priceRepository.save(newPrice);
    }

}
