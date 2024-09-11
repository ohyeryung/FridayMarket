package com.smile.fridaymarket_resource.domain.price.entity;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_PRICE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRICE_ID", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_TYPE", nullable = false)
    private OrderType orderType;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price; // g당 가격

    @Column(name = "CREATED_BY", columnDefinition = "UUID", nullable = false)
    private UUID createdBy; // 해당 가격을 등록한 유저 아이디
}
