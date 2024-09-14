package com.smile.fridaymarket_resource.domain.product.service;

import com.smile.fridaymarket_resource.domain.product.entity.Product;
import com.smile.fridaymarket_resource.domain.product.repository.ProductRepository;
import com.smile.fridaymarket_resource.global.exception.CustomException;
import com.smile.fridaymarket_resource.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReaderImpl implements ProductReader {

    private final ProductRepository productRepository;

    /**
     * 상품 조회
     *
     * @param productId 상품 Id
     * @return 해당 상품 객체 반환
     */
    public Product getProduct(Long productId) {

        // 상품 가져오기
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    }

}
