package com.fit.iuh.user_service.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fit.iuh.user_service.constant.RoleName;
import com.fit.iuh.user_service.model.Role;
import com.fit.iuh.user_service.repository.RoleRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Map<RoleName, String> roles = Map.of(
                RoleName.ADMIN, "Quản trị viên hệ thống",
                RoleName.CUSTOMER, "Người dùng hệ thống");

        roles.forEach((name, description) -> {
            if (!roleRepository.existsByName(name.toString())) {
                roleRepository.save(Role.builder()
                        .name(name.toString())
                        .description(description)
                        .build());
            }
        });
    }
}
