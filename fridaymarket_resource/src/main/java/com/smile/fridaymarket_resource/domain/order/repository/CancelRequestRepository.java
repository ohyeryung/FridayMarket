package com.smile.fridaymarket_resource.domain.order.repository;

import com.smile.fridaymarket_resource.domain.order.entity.CancelRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancelRequestRepository extends JpaRepository<CancelRequest, Long> {

    Optional<CancelRequest> findByOrderId(Long orderId);
}
