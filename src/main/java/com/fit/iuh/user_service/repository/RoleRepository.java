package com.fit.iuh.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fit.iuh.user_service.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {}
