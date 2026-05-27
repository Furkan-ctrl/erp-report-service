package com.erp.report_service.controller;

import com.erp.report_service.dto.MonthlySalesRow;
import com.erp.report_service.dto.TopProductRow;
import com.erp.report_service.service.ReportService;
import com.erp.report_service.util.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<MonthlySalesRow>> getMonthlySales(
            @RequestParam(required = false) Integer year,
            HttpServletRequest httpRequest) {
        RequestContext.requireAdmin(httpRequest);
        return ResponseEntity.ok(reportService.getMonthlySales(year));
    }


    @GetMapping("/monthly-sales/export")
    public ResponseEntity<byte[]> exportMonthlySales(
            @RequestParam(required = false) Integer year,
            HttpServletRequest httpRequest) {
        RequestContext.requireAdmin(httpRequest);

        byte[] excelBytes = reportService.exportMonthlySalesExcel(year);

        String filename = year != null
                ? "monthly_sales_" + year + ".xlsx"
                : "monthly_sales_all.xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument" +
                                ".spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(excelBytes);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductRow>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest httpRequest) {
        RequestContext.requireAdmin(httpRequest);
        return ResponseEntity.ok(reportService.getTopProducts(limit));
    }

    @GetMapping("/top-products/export")
    public ResponseEntity<byte[]> exportTopProducts(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest httpRequest) {
        RequestContext.requireAdmin(httpRequest);

        byte[] excelBytes = reportService.exportTopProductsExcel(limit);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument" +
                                ".spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"top_products_top" + limit + ".xlsx\"")
                .body(excelBytes);
    }
}