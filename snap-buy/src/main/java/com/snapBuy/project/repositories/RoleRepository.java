package com.snapBuy.project.repositories;

import com.snapBuy.project.model.AppRole;
import com.snapBuy.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}