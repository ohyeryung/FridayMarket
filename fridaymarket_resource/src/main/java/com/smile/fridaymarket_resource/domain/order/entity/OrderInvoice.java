package com.smile.fridaymarket_resource.domain.order.entity;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_ORDER_INVOICE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInvoice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_INVOICE_ID", nullable = false)
    private Long id;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "USER_ID", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_TYPE", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "DELIVERY_ADDRESS", nullable = false)
    private String deliveryAddress;

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    // OrderProduct와 일대다 관계 설정
    @OneToMany(mappedBy = "orderInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    // 주문 저장 이후 orderNo 생성
    @PostPersist
    public void generateOrderNo() {

        String orderTypePrefix = orderType.toString(); // "BUY" or "SELL"
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")); // 현재 날짜 (월일)
        String orderIdSuffix = String.format("%04d", this.id); // 주문 ID 값 형식 지정 (4자리)

        this.orderNo = orderTypePrefix + "-" + date + "-" + orderIdSuffix;
    }

    // 입금 완료 상태 값 변경
    public void updateStatusIsPaymentReceived() {

        this.orderStatus = OrderStatus.PAYMENT_RECEIVED;
    }

    // 발송 완료 상태 값 변경
    public void updateStatusIsShipped() {

        this.orderStatus = OrderStatus.SHIPPED;
    }

    // 수령 완료 상태 값 변경
    public void updateStatusIsReceived() {

        this.orderStatus = OrderStatus.RECEIVED;
    }

    // 송금 완료 상태 값 변경
    public void updateStatusIsPaymentSent() {

        this.orderStatus = OrderStatus.PAYMENT_SENT;
    }

    // 주문 취소 상태 값 변경
    public void updateStatusCanceled() {

        this.isDeleted = true;
        this.orderStatus = OrderStatus.CANCELED;
        this.softDelete();
    }

    // 계산된 주문 총액 업데이트
    public void updateAmount(BigDecimal totalAmount) {

        this.amount = totalAmount;
    }

    // 주문 번호 최종 업데이트
    public void updateOrderNo(String orderNo) {

        this.orderNo = orderNo;
    }

}
