package com.medoc.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medoc.ordo.OrdoRepository;
import com.medoc.user.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReportController {

    @Autowired
    private OrdoRepository ordoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/report")
    public String viewReportPage(
            @RequestParam(value = "period", defaultValue = "month") String period,
            Model model) {
        
        // Get total counts
        long totalOrders = ordoRepository.count();
        long totalUsers = userRepository.count();
        long activeOrders = ordoRepository.findAllEnabled().size();
        long pendingOrders = totalOrders - activeOrders;
        
        // Calculate growth (simplified - you can enhance this with actual date-based queries)
        double orderGrowth = totalOrders > 0 ? 15.5 : 0; // Placeholder
        double userGrowth = totalUsers > 0 ? 8.3 : 0; // Placeholder
        
        // Get current date
        LocalDate today = LocalDate.now();
        String currentDate = today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        // Add attributes to model
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeOrders", activeOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("orderGrowth", orderGrowth);
        model.addAttribute("userGrowth", userGrowth);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("selectedPeriod", period);
        
        // Generate chart data (last 7 days for example)
        List<String> chartLabels = new ArrayList<>();
        List<Long> chartData = new ArrayList<>();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            chartLabels.add(date.format(DateTimeFormatter.ofPattern("MMM dd")));
            // Simplified: You should query actual data per day
            chartData.add((long) (Math.random() * 10 + 5));
        }
        
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);
        
        return "report/report";
    }
    
    @GetMapping("/report/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sales_report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        
        // Get data
        long totalOrders = ordoRepository.count();
        long totalUsers = userRepository.count();
        long activeOrders = ordoRepository.findAllEnabled().size();
        long pendingOrders = totalOrders - activeOrders;
        
        ReportPDFExporter exporter = new ReportPDFExporter(totalOrders, totalUsers, activeOrders, pendingOrders);
        exporter.export(response);
    }
    
    @GetMapping("/report/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sales_report_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        // Get data
        long totalOrders = ordoRepository.count();
        long totalUsers = userRepository.count();
        long activeOrders = ordoRepository.findAllEnabled().size();
        long pendingOrders = totalOrders - activeOrders;
        
        ReportExcelExporter exporter = new ReportExcelExporter();
        exporter.export(response, totalOrders, totalUsers, activeOrders, pendingOrders);
    }
}
