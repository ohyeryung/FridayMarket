package com.smile.fridaymarket_resource.domain.order.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smile.fridaymarket_resource.domain.order.dto.OrderList;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static com.smile.fridaymarket_resource.domain.order.entity.QOrderInvoice.orderInvoice;
import static com.smile.fridaymarket_resource.domain.order.entity.QOrderProduct.orderProduct;

public class OrderRepositoryQueryDslImpl implements OrderRepositoryQueryDsl {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OrderList> getOrderList(String userId, Pageable pageable) {

        List<OrderList> orderLists = queryFactory
                .select(Projections.constructor(OrderList.class,
                        orderInvoice.id.as("orderId"),
                        orderInvoice.orderType,
                        orderProduct.product.productName,  // 첫 번째 상품만 가져오기
                        orderInvoice.amount.as("totalAmount"),
                        orderInvoice.orderStatus,
                        orderInvoice.createdAt,
                        orderInvoice.updatedAt,
                        orderInvoice.deletedAt,
                        orderInvoice.isDeleted
                ))
                .from(orderInvoice)
                .join(orderInvoice.orderProducts, orderProduct)
                .where(orderInvoice.userId.eq(UUID.fromString(userId))
                        .and(orderInvoice.isDeleted.eq(false)))
                .groupBy(orderInvoice.id)  // 각 주문서별로 첫 번째 상품만 가져오도록 그룹화
                .orderBy(orderInvoice.createdAt.desc())  // 최신 주문 순으로 정렬
                .fetch();

        long total = queryFactory
                .selectFrom(orderInvoice)
                .where(orderInvoice.userId.eq(UUID.fromString(userId)))
                .fetchCount();

        return new PageImpl<>(orderLists, pageable, total);
    }

}
