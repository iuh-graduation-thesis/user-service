package com.fit.iuh.user_service.dto.response.role;

import lombok.Builder;

import java.util.Set;

import com.fit.iuh.user_service.dto.response.permission.PermissionResponse;

/**
 * Admin 2/18/2026
 *
 **/
@Builder
public record RoleResponse(
        String id,
        String name,
        String description,
        Set<PermissionResponse> permissions
) {
}
