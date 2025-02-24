package dev.tgsi.attendance_registration_system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceDto {

    private Long attendanceId;

    private String timeIn;

    private String timeOut;

    private String remarks;

    public AttendanceDto(Long attendanceId, String timeIn, String timeOut, String remarks) {
        this.attendanceId = attendanceId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.remarks = remarks;
    }


}
