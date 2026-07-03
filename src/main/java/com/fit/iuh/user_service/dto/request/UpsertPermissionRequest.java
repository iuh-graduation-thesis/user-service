package com.fit.iuh.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Admin 2/16/2026
 *
 **/
public record UpsertPermissionRequest(

        @NotBlank(message = "Tên quyền không được để trống")
        String name,

        String description
) {
}
