package dev.tgsi.attendance_registration_system.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class LeaveDto {
    
    private String leaveDateDto;

    private String leaveTypeDto;

    private String leaveDurationDto;
    
    private String remarksDto;

    public LeaveDto(String leaveDate, String leaveType, String leaveDuration, String remarks) {
        this.leaveDateDto = leaveDate;
        this.leaveTypeDto = leaveType;
        this.leaveDurationDto = leaveDuration;
        this.remarksDto = remarks;
    }

    
}
