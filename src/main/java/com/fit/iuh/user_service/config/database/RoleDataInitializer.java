package com.fit.iuh.user_service.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fit.iuh.user_service.constant.RoleConstants;
import com.fit.iuh.user_service.model.Role;
import com.fit.iuh.user_service.repository.RoleRepository;

import java.util.Map;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Map<String, String> roles = Map.of(
                RoleConstants.ADMIN, "Quản trị viên hệ thống",
                RoleConstants.CUSTOMER, "Người dùng hệ thống");

        roles.forEach((name, description) -> {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(Role.builder()
                        .name(name)
                        .description(description)
                        .build());
            }
        });
    }
}
