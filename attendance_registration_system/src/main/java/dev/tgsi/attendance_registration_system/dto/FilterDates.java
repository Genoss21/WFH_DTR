package dev.tgsi.attendance_registration_system.dto;

import lombok.Data;

@Data
// * For filter dates purposes
public class FilterDates {

    private String startDate;

    private String endDate;
    
}
