package com.smile.fridaymarket_resource.domain.product.entity;

import com.smile.fridaymarket_resource.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "TB_PRODUCT")
@Getter
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_NAME", nullable = false)
    private ProductName productName;

}
