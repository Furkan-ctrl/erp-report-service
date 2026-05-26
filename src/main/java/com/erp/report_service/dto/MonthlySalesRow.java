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
    private int year;
    private int month;
    private long orderCount;
    private BigDecimal totalRevenue;
    private BigDecimal avgOrderValue;
}
