package com.fit.iuh.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpsertPermissionRequest(

        @NotBlank(message = "Tên quyền không được để trống")
        String name,

        String description
) {
}
