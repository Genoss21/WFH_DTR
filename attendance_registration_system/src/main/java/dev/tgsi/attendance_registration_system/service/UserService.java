package dev.tgsi.attendance_registration_system.service;

import dev.tgsi.attendance_registration_system.dto.UserDto;
import dev.tgsi.attendance_registration_system.models.User;

public interface UserService {
	
	User save (UserDto userDto);
	

}
