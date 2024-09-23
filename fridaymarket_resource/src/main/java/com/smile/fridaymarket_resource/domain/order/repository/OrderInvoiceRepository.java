package com.smile.fridaymarket_resource.domain.order.repository;

import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.repository.querydsl.OrderRepositoryQueryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, Long>, OrderRepositoryQueryDsl {
}
