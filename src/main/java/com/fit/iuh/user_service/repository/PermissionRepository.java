package com.fit.iuh.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fit.iuh.user_service.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);
}
