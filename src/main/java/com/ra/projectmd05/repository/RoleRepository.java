package com.ra.projectmd05.repository;

import com.ra.projectmd05.constants.RoleName;
import com.ra.projectmd05.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(RoleName roleName);
}
