package dev.tgsi.attendance_registration_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TargetDateTime {
    
    public LocalDate getTargetDate(){
        LocalDate date = LocalDate.parse(LocalDate.now().toString());
        return date;
    }

     public LocalDateTime getCurrendtDateTime(){

        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().toString());
        return now;

    }

    public LocalDateTime getResetDate(){

        LocalDate date;
        LocalTime time = LocalTime.of(6, 00, 0);
        LocalDateTime dateTime;
        
        date = LocalDate.now();
        dateTime = date.atTime(time);
        return dateTime;
    }
    public LocalDate getNextDay(){

        LocalDate date = getTargetDate().plusDays(1);
        return date;
    }

    public LocalDate getPreviousDay(){

        return getTargetDate().minusDays(1);

    }

}
