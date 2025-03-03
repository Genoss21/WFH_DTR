package dev.tgsi.attendance_registration_system.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;

@Service
public class ExcelExportService {
    
    public byte[] exportToExcel(List<AttendanceRecord> records, String username, String empId, String email) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Records");
            
            // Create formatters for date and time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            // Create header row style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Create headers
            Row headerRow = sheet.createRow(4);
            String[] headers = {"Employee ID", "Full Name", "Email", "Date", "Time In", "Time Out", "Edited By", "Remarks"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }
            
            // Fill data
            int rowNum = 5;
            for (AttendanceRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                
                // Get user's full name from the record
                String fullName = "";
                if (record.getUser() != null && record.getUser().getPersonalInfo() != null) {
                    fullName = record.getUser().getPersonalInfo().getFirstName() + " " + 
                             record.getUser().getPersonalInfo().getLastName();
                } else {
                    fullName = record.getUser().getUsername();
                }
                
                row.createCell(0).setCellValue(empId);
                row.createCell(1).setCellValue(fullName);
                row.createCell(2).setCellValue(email);
                row.createCell(3).setCellValue(record.getDate().toString());
                row.createCell(4).setCellValue(record.getTimeIn() != null ? record.getTimeIn().format(timeFormatter) : "");
                row.createCell(5).setCellValue(record.getTimeOut() != null ? record.getTimeOut().format(timeFormatter) : "");
                row.createCell(6).setCellValue(record.getEditedByName() != null ? record.getEditedByName() : "");
                row.createCell(7).setCellValue(record.getRemarks() != null ? record.getRemarks() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
