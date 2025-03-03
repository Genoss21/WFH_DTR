package dev.tgsi.attendance_registration_system.service;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(List<AttendanceRecord> records, String username, String empId, String email) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Records");

            // Create header row styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Create user info rows
            Row userInfoRow1 = sheet.createRow(0);
            userInfoRow1.createCell(0).setCellValue("Employee ID:");
            userInfoRow1.createCell(1).setCellValue(empId);
            
            Row userInfoRow2 = sheet.createRow(1);
            userInfoRow2.createCell(0).setCellValue("Username:");
            userInfoRow2.createCell(1).setCellValue(username);
            
            Row userInfoRow3 = sheet.createRow(2);
            userInfoRow3.createCell(0).setCellValue("Email:");
            userInfoRow3.createCell(1).setCellValue(email);

            // Create headers
            String[] headers = {"Date", "Time In", "Time Out", "Edited By", "Remarks"};
            Row headerRow = sheet.createRow(4);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            int rowNum = 5;
            
            for (AttendanceRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getDate().format(dateFormatter));
                row.createCell(1).setCellValue(record.getTimeIn() != null ? record.getTimeIn().format(timeFormatter) : "");
                row.createCell(2).setCellValue(record.getTimeOut() != null ? record.getTimeOut().format(timeFormatter) : "");
                row.createCell(3).setCellValue(record.getUser().getUsername());
                row.createCell(4).setCellValue(record.getRemarks() != null ? record.getRemarks() : "");
            }

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
