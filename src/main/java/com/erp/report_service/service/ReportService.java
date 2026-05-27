package com.erp.report_service.service;

import com.erp.report_service.excel.ExcelGenerator;
import com.erp.report_service.repository.OrderItemSummaryRepository;
import com.erp.report_service.repository.OrderSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.erp.report_service.dto.MonthlySalesRow;
import com.erp.report_service.dto.TopProductRow;;
import com.erp.report_service.exception.ReportException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final OrderSummaryRepository orderSummaryRepository;
    private final OrderItemSummaryRepository orderItemSummaryRepository;
    private final ExcelGenerator excelGenerator;

    @Transactional(readOnly = true)
    public List<MonthlySalesRow> getMonthlySales(Integer year) {
        if (year != null) {
            log.info("Generating monthly sales report for year {}", year);
            return orderSummaryRepository.getMonthlySalesByYear(year);
        }
        log.info("Generating monthly sales report for all time");
        return orderSummaryRepository.getMonthlySales();
    }

    @Transactional(readOnly = true)
    public List<TopProductRow> getTopProducts(int limit) {
        if (limit < 1 || limit > 100) {
            throw new ReportException(
                    "Limit must be between 1 and 100",
                    HttpStatus.BAD_REQUEST
            );
        }

        List<TopProductRow> rows = orderItemSummaryRepository
                .getTopProducts(limit);

        List<TopProductRow> ranked = IntStream
                .range(0, Math.min(rows.size(), limit))
                .mapToObj(i -> {
                    TopProductRow row = rows.get(i);
                    row.setRank(i + 1);
                    return row;
                })
                .toList();

        log.info("Top {} products report generated", ranked.size());
        return ranked;
    }

    @Transactional(readOnly = true)
    public byte[] exportMonthlySalesExcel(Integer year) {
        List<MonthlySalesRow> rows = getMonthlySales(year);

        if (rows.isEmpty()) {
            throw new ReportException(
                    "No sales data found" + (year != null ? " for year " + year : ""),
                    HttpStatus.NOT_FOUND
            );
        }

        try {
            return excelGenerator.generateMonthlySalesReport(rows);
        } catch (IOException e) {
            log.error("Excel generation failed: {}", e.getMessage());
            throw new ReportException(
                    "Failed to generate Excel report",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional(readOnly = true)
    public byte[] exportTopProductsExcel(int limit) {
        List<TopProductRow> rows = getTopProducts(limit);

        if (rows.isEmpty()) {
            throw new ReportException(
                    "No product sales data found",
                    HttpStatus.NOT_FOUND
            );
        }

        try {
            return excelGenerator.generateTopProductsReport(rows);
        } catch (IOException e) {
            log.error("Excel generation failed: {}", e.getMessage());
            throw new ReportException(
                    "Failed to generate Excel report",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}

