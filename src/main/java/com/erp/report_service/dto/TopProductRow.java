package com.erp.report_service.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductRow {
    private int rank;
    private String productName;
    private String productSku;
    private long totalQuantitySold;
    private BigDecimal totalRevenue;
}