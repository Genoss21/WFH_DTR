package dev.tgsi.attendance_registration_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchDto {
    private String fullName;
    private String email;
    private String imgSrc;
    private String status; // e.g. ONLINE, OFFLINE, or ON_LEAVE
}
