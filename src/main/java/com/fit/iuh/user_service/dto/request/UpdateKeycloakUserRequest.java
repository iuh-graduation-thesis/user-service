package com.fit.iuh.user_service.dto.request;

import lombok.Builder;

@Builder
public record UpdateKeycloakUserRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean enabled
) {
    public boolean hasAnyValue() {
        return hasText(username)
                || hasText(email)
                || hasText(firstName)
                || hasText(lastName)
                || enabled != null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
