package com.fit.iuh.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fit.iuh.user_service.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {}
