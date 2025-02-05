package dev.tgsi.attendance_registration_system.repository;

import dev.tgsi.attendance_registration_system.models.RoleModel;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    
    RoleModel findByRoleId (int roleId);

    
}
