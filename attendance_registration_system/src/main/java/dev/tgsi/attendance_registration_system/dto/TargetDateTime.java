package dev.tgsi.attendance_registration_system.dto;

import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.LocalTime;

public class TargetDateTime {
    
    public LocalDate getTargetDate(){
        
        LocalTime timeOut = LocalTime.now();
        LocalDate date;
        //LocalTime time = LocalTime.of(5, 0, 0);;
       // LocalDateTime dateTime;
        if(timeOut.isBefore(LocalTime.of(5, 1, 0))){
            date = LocalDate.now().minusDays(1);
            //dateTime = date.atTime(time);
        }
        else{
            date = LocalDate.now().plusDays(1);
            ///dateTime = date.atTime(time);
        }

        return date;
    }

    public LocalDate getBeforeDate(){

        return getTargetDate().minusDays(1);
    }


}
