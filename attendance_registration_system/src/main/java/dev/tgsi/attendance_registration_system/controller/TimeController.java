package dev.tgsi.attendance_registration_system.controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class TimeController {

    @GetMapping("/server-time")
    public Map<String, Object> getServerTime() {

        ZoneId serverTimeZone = ZoneId.systemDefault();
        ZonedDateTime serverDateTime = ZonedDateTime.now(serverTimeZone);
        return Map.of(
            "dayOfWeek",serverDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.ENGLISH),
            "hour",serverDateTime.getHour(),
            "minutes",serverDateTime.getMinute(),
            "seconds",serverDateTime.getSecond()
        );

    }
    
    
}
