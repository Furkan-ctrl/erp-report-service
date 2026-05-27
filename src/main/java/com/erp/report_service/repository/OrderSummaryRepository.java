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
            CAST(YEAR(o.createdAt) AS Integer),
            CAST(MONTH(o.createdAt) AS Integer),
            COUNT(o.id),
            SUM(o.totalAmount),
            CAST(AVG(o.totalAmount) AS Double)
        )
        FROM OrderSummary o
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
        GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)
        ORDER BY YEAR(o.createdAt) DESC, MONTH(o.createdAt) DESC
        """)
    List<MonthlySalesRow> getMonthlySales();

    @Query("""
        SELECT new com.erp.report_service.dto.MonthlySalesRow(
            CAST(YEAR(o.createdAt) AS Integer),
            CAST(MONTH(o.createdAt) AS Integer),
            COUNT(o.id),
            SUM(o.totalAmount),
            CAST(AVG(o.totalAmount) AS Double)
        )
        FROM OrderSummary o
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
          AND YEAR(o.createdAt) = :year
        GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)
        ORDER BY MONTH(o.createdAt) ASC
        """)
    List<MonthlySalesRow> getMonthlySalesByYear(@Param("year") int year);
}
