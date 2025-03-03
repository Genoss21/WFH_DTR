package dev.tgsi.attendance_registration_system.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.util.CellRangeAddress;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;

@Service
public class ExcelExportService {
    
    public byte[] exportToExcel(List<AttendanceRecord> records, String username, String empId, String email) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Records");
            
            // Create formatters for date and time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            // Create company name header
            Row companyRow = sheet.createRow(0);
            Cell companyCell = companyRow.createCell(0);
            companyCell.setCellValue("TSUKIDEN GLOBAL SOLUTIONS, INC.");
            
            // Create header style for company name
            CellStyle companyStyle = workbook.createCellStyle();
            Font companyFont = workbook.createFont();
            companyFont.setBold(true);
            companyFont.setFontHeightInPoints((short) 14);
            companyStyle.setFont(companyFont);
            companyStyle.setAlignment(HorizontalAlignment.CENTER);
            companyCell.setCellStyle(companyStyle);
            
            // Merge cells for company name
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            
            // Add spacing
            sheet.createRow(1);
            sheet.createRow(2);
            sheet.createRow(3);
            
            // Create header row style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Create data cell style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            
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
                
                // Create and style each cell
                Cell[] cells = new Cell[8];
                cells[0] = row.createCell(0);
                cells[0].setCellValue(empId);
                cells[1] = row.createCell(1);
                cells[1].setCellValue(fullName);
                cells[2] = row.createCell(2);
                cells[2].setCellValue(email);
                cells[3] = row.createCell(3);
                cells[3].setCellValue(record.getDate().toString());
                cells[4] = row.createCell(4);
                cells[4].setCellValue(record.getTimeIn() != null ? record.getTimeIn().format(timeFormatter) : "");
                cells[5] = row.createCell(5);
                cells[5].setCellValue(record.getTimeOut() != null ? record.getTimeOut().format(timeFormatter) : "");
                cells[6] = row.createCell(6);
                cells[6].setCellValue(record.getEditedByName() != null ? record.getEditedByName() : "");
                cells[7] = row.createCell(7);
                cells[7].setCellValue(record.getRemarks() != null ? record.getRemarks() : "");
                
                // Apply style to all cells
                for (Cell cell : cells) {
                    cell.setCellStyle(dataStyle);
                }
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
