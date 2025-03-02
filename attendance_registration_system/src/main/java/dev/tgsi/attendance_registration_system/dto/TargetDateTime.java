package dev.tgsi.attendance_registration_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


// * For querying purpose to get standard date and time.
public class TargetDateTime {

    ZoneId serverTimeZone = ZoneId.systemDefault();
    ZonedDateTime serverDateTime = ZonedDateTime.now(serverTimeZone);
    DateTimeFormatter formatterDate =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterTime =  DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter formatterDateTime =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDate getDateNow(){

        LocalDate date = LocalDate.parse((serverDateTime.format(formatterDate).toString()),formatterDate);
        return date;
    }

    public LocalTime getTimeNow(){

        LocalTime time = LocalTime.parse((serverDateTime.format(formatterTime).toString()),formatterTime);
        return time;
    }

     public LocalDateTime getCurrentDateTime(){

        LocalDateTime now = LocalDateTime.parse((serverDateTime.format(formatterDateTime).toString()),formatterDateTime);
        return now;

    }

    public LocalDateTime getResetDate(){

        LocalDate date;
        LocalTime time = LocalTime.of(6, 00, 0);
        LocalDateTime dateTime;
        
        date = LocalDate.parse((serverDateTime.format(formatterDate).toString()),formatterDate);
        dateTime = date.atTime(time);
        return dateTime;
    }
    public LocalDate getNextDay(){

        LocalDate date = getDateNow().plusDays(1);
        return date;
    }

    public LocalDate getPreviousDay(){

        return getDateNow().minusDays(1);

    }

}
