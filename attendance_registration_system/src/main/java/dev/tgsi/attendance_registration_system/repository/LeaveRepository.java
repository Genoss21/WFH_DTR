package dev.tgsi.attendance_registration_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.LeaveModel;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveModel, Integer> {

    LeaveModel findByLeaveId(Integer leaveId);
}
