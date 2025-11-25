package com.medoc.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ReportExcelExporter {
    
    private XSSFWorkbook workbook;
    private Sheet sheet;
    
    public ReportExcelExporter() {
        workbook = new XSSFWorkbook();
    }
    
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Sales Report");
        Row row = sheet.createRow(0);
        
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 240);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        createCell(row, 0, "Metric", style);
        createCell(row, 1, "Value", style);
        createCell(row, 2, "Status", style);
    }
    
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        
        cell.setCellStyle(style);
    }
    
    private void writeDataLines(long totalOrders, long totalUsers, long activeOrders, long pendingOrders) {
        int rowCount = 1;
        
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeight((short) 200);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Total Orders
        Row row = sheet.createRow(rowCount++);
        createCell(row, 0, "Total Orders", style);
        createCell(row, 1, totalOrders, style);
        createCell(row, 2, "Active", style);
        
        // Total Users
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Total Users", style);
        createCell(row, 1, totalUsers, style);
        createCell(row, 2, "Registered", style);
        
        // Active Orders
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Active Orders", style);
        createCell(row, 1, activeOrders, style);
        createCell(row, 2, "Completed", style);
        
        // Pending Orders
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Pending Orders", style);
        createCell(row, 1, pendingOrders, style);
        createCell(row, 2, "In Progress", style);
        
        // Add summary row
        row = sheet.createRow(rowCount + 1);
        CellStyle summaryStyle = workbook.createCellStyle();
        Font summaryFont = workbook.createFont();
        summaryFont.setBold(true);
        summaryFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        summaryStyle.setFont(summaryFont);
        
        createCell(row, 0, "Report Generated", summaryStyle);
        createCell(row, 1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), summaryStyle);
    }
    
    public void export(HttpServletResponse response, long totalOrders, long totalUsers, 
                      long activeOrders, long pendingOrders) throws IOException {
        writeHeaderLine();
        writeDataLines(totalOrders, totalUsers, activeOrders, pendingOrders);
        
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
