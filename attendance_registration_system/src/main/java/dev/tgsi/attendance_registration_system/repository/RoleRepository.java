package dev.tgsi.attendance_registration_system.repository;

import dev.tgsi.attendance_registration_system.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
}
