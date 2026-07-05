package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;

public interface KeycloakUserService {

    void updateNameIfChanged(String userId, String firstName, String lastName);

    void updatePassword(String userId, String username, UpdatePasswordRequest request);
}
