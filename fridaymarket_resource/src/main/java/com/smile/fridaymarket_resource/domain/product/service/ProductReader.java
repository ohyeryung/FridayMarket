package com.smile.fridaymarket_resource.domain.product.service;

import com.smile.fridaymarket_resource.domain.product.entity.Product;

public interface ProductReader {

    Product getProduct(Long productId);

}
