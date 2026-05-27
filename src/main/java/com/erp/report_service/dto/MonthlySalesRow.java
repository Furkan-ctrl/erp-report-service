package com.erp.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalesRow {
    private Integer year;
    private Integer month;
    private Long orderCount;
    private BigDecimal totalRevenue;
    private Double avgOrderValue;
}
