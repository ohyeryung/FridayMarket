package com.smile.fridaymarket_resource.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// 배송 완료 요청 엔티티
@Entity
@Table(name = "TB_CNACEL_REQUEST")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;  // 요청한 사용자 ID

    private Long orderId; // 주문 ID

    private LocalDateTime requestedAt; // 요청 시간

    private boolean isApproved; // 승인 여부 (관리자가 변경)

    public void updateStatus() {
        this.isApproved = true;

    }

}
