package com.fit.iuh.user_service.controller.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fit.iuh.user_service.dto.base.ApiResponse;
import com.fit.iuh.user_service.dto.response.UserPermissionsResponse;
import com.fit.iuh.user_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserInternalController {
    
    UserService userService;

    @GetMapping("/{keycloakId}/permissions")
    public ApiResponse<UserPermissionsResponse> getUserPermissions(@PathVariable String keycloakId) {
        return ApiResponse.<UserPermissionsResponse>builder()
                .result(userService.getUserPermissions(keycloakId))
                .build();
    }
}
