package com.erp.report_service.repository;

import com.erp.report_service.dto.MonthlySalesRow;
import com.erp.report_service.model.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long> {

    @Query("""
        SELECT new com.erp.report_service.dto.MonthlySalesRow(
            FUNCTION('YEAR',  o.createdAt),
            FUNCTION('MONTH', o.createdAt),
            COUNT(o.id),
            SUM(o.totalAmount),
            AVG(o.totalAmount)
        )
        FROM OrderSummary o
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
        GROUP BY FUNCTION('YEAR', o.createdAt),
                 FUNCTION('MONTH', o.createdAt)
        ORDER BY FUNCTION('YEAR', o.createdAt) DESC,
                 FUNCTION('MONTH', o.createdAt) DESC
        """)
    List<MonthlySalesRow> getMonthlySales();

    @Query("""
        SELECT new com.erp.report_service.dto.MonthlySalesRow(
            FUNCTION('YEAR',  o.createdAt),
            FUNCTION('MONTH', o.createdAt),
            COUNT(o.id),
            SUM(o.totalAmount),
            AVG(o.totalAmount)
        )
        FROM OrderSummary o
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
          AND FUNCTION('YEAR', o.createdAt) = :year
        GROUP BY FUNCTION('YEAR', o.createdAt),
                 FUNCTION('MONTH', o.createdAt)
        ORDER BY FUNCTION('MONTH', o.createdAt) ASC
        """)
    List<MonthlySalesRow> getMonthlySalesByYear(@Param("year") int year);
}
