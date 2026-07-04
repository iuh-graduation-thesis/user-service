package com.fit.iuh.user_service.dto.response;

import lombok.Builder;

@Builder
public record PermissionResponse(
        String id,
        String name,
        String description) {
}
