package dev.tgsi.attendance_registration_system.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class AttendanceDto {
    
    private String empId;

    private String fullName;

    private String email;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String editedBy;

    private String remarks;

    public AttendanceDto(String empId, String fullName, String email, LocalDate date, LocalTime timeIn,
            LocalTime timeOut, String editedBy, String remarks) {
        this.empId = empId;
        this.fullName = fullName;
        this.email = email;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.editedBy = editedBy;
        this.remarks = remarks;
    }

}
