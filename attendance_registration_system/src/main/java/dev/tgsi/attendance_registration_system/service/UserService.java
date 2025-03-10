package dev.tgsi.attendance_registration_system.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.repository.PersonalInfoRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.models.PersonalInfoModel;
import dev.tgsi.attendance_registration_system.models.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    

    public String getUserImagePath(String empId) {
        Optional<User> userOptional = userRepository.findById(empId);
        return userOptional.map(User::getImgSrc).orElse(null);
    }

    public List<PersonalInfoModel> getAllEmployees() {
        return personalInfoRepository.findAll();
}
}
