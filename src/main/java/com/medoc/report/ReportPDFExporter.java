package com.medoc.report;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

public class ReportPDFExporter {
    
    private long totalOrders;
    private long totalUsers;
    private long activeOrders;
    private long pendingOrders;
    
    public ReportPDFExporter(long totalOrders, long totalUsers, long activeOrders, long pendingOrders) {
        this.totalOrders = totalOrders;
        this.totalUsers = totalUsers;
        this.activeOrders = activeOrders;
        this.pendingOrders = pendingOrders;
    }
    
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(0, 129, 113));
        cell.setPadding(10);
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);
        font.setSize(12);
        
        cell.setPhrase(new Phrase("Metric", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Value", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
    }
    
    private void writeTableData(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(11);
        
        // Total Orders
        PdfPCell cell = new PdfPCell(new Phrase("Total Orders", font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalOrders), font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Active", font));
        cell.setPadding(8);
        cell.setBackgroundColor(new Color(232, 249, 246));
        table.addCell(cell);
        
        // Total Users
        cell = new PdfPCell(new Phrase("Total Users", font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalUsers), font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Registered", font));
        cell.setPadding(8);
        cell.setBackgroundColor(new Color(232, 249, 246));
        table.addCell(cell);
        
        // Active Orders
        cell = new PdfPCell(new Phrase("Active Orders", font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(activeOrders), font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Completed", font));
        cell.setPadding(8);
        cell.setBackgroundColor(new Color(232, 249, 246));
        table.addCell(cell);
        
        // Pending Orders
        cell = new PdfPCell(new Phrase("Pending Orders", font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(pendingOrders), font));
        cell.setPadding(8);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("In Progress", font));
        cell.setPadding(8);
        cell.setBackgroundColor(new Color(237, 137, 54));
        table.addCell(cell);
    }
    
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        
        document.open();
        
        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(24);
        titleFont.setColor(new Color(0, 129, 113));
        
        Paragraph title = new Paragraph("Sales Report", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Date
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA);
        dateFont.setSize(12);
        dateFont.setColor(Color.GRAY);
        
        String currentDate = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss").format(new Date());
        Paragraph dateParagraph = new Paragraph("Generated on: " + currentDate, dateFont);
        dateParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        dateParagraph.setSpacingAfter(30);
        document.add(dateParagraph);
        
        // Table
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {3.0f, 2.0f, 2.5f});
        table.setSpacingBefore(10);
        
        writeTableHeader(table);
        writeTableData(table);
        
        document.add(table);
        
        // Summary
        Paragraph summary = new Paragraph();
        summary.setSpacingBefore(30);
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA);
        summaryFont.setSize(11);
        
        summary.add(new Phrase("\n\nSummary:\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 129, 113))));
        summary.add(new Phrase("• Total number of orders in the system: " + totalOrders + "\n", summaryFont));
        summary.add(new Phrase("• Total number of registered users: " + totalUsers + "\n", summaryFont));
        summary.add(new Phrase("• Orders currently active: " + activeOrders + "\n", summaryFont));
        summary.add(new Phrase("• Orders pending processing: " + pendingOrders + "\n", summaryFont));
        
        double completionRate = totalOrders > 0 ? (double) activeOrders / totalOrders * 100 : 0;
        summary.add(new Phrase("• Order completion rate: " + String.format("%.2f", completionRate) + "%\n", summaryFont));
        
        document.add(summary);
        
        // Footer
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        footerFont.setSize(9);
        footerFont.setColor(Color.GRAY);
        
        Paragraph footer = new Paragraph();
        footer.setSpacingBefore(40);
        footer.add(new Phrase("\n\n_______________________________________________\n", footerFont));
        footer.add(new Phrase("MeDocs - Medical Document Management System\n", footerFont));
        footer.add(new Phrase("© 2025 MeDocs. All rights reserved.", footerFont));
        footer.setAlignment(Paragraph.ALIGN_CENTER);
        
        document.add(footer);
        
        document.close();
    }
}
