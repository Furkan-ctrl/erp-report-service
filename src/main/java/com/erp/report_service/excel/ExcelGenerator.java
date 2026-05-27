package com.erp.report_service.excel;

import com.erp.report_service.dto.MonthlySalesRow;
import com.erp.report_service.dto.TopProductRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
public class ExcelGenerator {

    public byte[] generateMonthlySalesReport(
            List<MonthlySalesRow> rows) throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Sales");

            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle currencyStyle = buildCurrencyStyle(workbook);
            CellStyle numberStyle = buildNumberStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Year", "Month", "Order Count",
                    "Total Revenue ($)", "Avg Order Value ($)"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (MonthlySalesRow row : rows) {
                Row dataRow = sheet.createRow(rowIndex++);

                createCell(dataRow, 0, row.getYear(), numberStyle);
                createCell(dataRow, 1, row.getMonth(), numberStyle);
                createCell(dataRow, 2, row.getOrderCount(), numberStyle);
                createCell(dataRow, 3,
                        row.getTotalRevenue().doubleValue(), currencyStyle);
                createCell(dataRow, 4,
                        row.getAvgOrderValue() != null ? row.getAvgOrderValue() : 0.0,
                        currencyStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            log.info("Monthly sales Excel generated: {} rows", rows.size());
            return out.toByteArray();
        }
    }


    public byte[] generateTopProductsReport(

            List<TopProductRow> rows) throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Top Products");

            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle currencyStyle = buildCurrencyStyle(workbook);
            CellStyle numberStyle = buildNumberStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Rank", "Product Name", "SKU",
                    "Total Qty Sold", "Total Revenue ($)"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (TopProductRow row : rows) {
                Row dataRow = sheet.createRow(rowIndex++);

                createCell(dataRow, 0, row.getRank(), numberStyle);
                createStringCell(dataRow, 1, row.getProductName());
                createStringCell(dataRow, 2, row.getProductSku());
                createCell(dataRow, 3, row.getTotalQuantitySold(), numberStyle);
                createCell(dataRow, 4,
                        row.getTotalRevenue().doubleValue(), currencyStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            log.info("Top products Excel generated: {} rows", rows.size());
            return out.toByteArray();
        }
    }

    // ------------Private helpers-------------------

    private CellStyle buildHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle buildCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    private CellStyle buildNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }

    private void createCell(Row row, int col, long value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int col, double value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createStringCell(Row row, int col, String value) {
        row.createCell(col).setCellValue(value != null ? value : "");
    }
}