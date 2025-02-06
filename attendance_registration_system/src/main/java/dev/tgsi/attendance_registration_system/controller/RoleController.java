package dev.tgsi.attendance_registration_system.controller;

import dev.tgsi.attendance_registration_system.models.RoleModel;
import dev.tgsi.attendance_registration_system.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public RoleModel getRoleById(@PathVariable("id") Long id) {
        return roleService.getRoleById(id);
    }
}
