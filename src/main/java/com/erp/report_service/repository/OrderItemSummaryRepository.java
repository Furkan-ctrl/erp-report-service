package com.erp.report_service.repository;

import com.erp.report_service.dto.TopProductRow;
import com.erp.report_service.model.OrderItemSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemSummaryRepository extends JpaRepository<OrderItemSummary, Long> {
    @Query("""
        SELECT new com.erp.report_service.dto.TopProductRow(
            0,
            oi.productName,
            oi.productSku,
            SUM(oi.quantity),
            SUM(oi.subtotal)
        )
        FROM OrderItemSummary oi
        JOIN OrderSummary o ON o.id = oi.orderId
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
        GROUP BY oi.productName, oi.productSku
        ORDER BY SUM(oi.subtotal) DESC
        LIMIT :limit
        """)
    List<TopProductRow> getTopProducts(@Param("limit") int limit);
}
