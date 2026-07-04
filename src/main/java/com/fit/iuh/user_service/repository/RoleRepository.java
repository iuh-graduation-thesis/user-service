package com.fit.iuh.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fit.iuh.user_service.model.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
