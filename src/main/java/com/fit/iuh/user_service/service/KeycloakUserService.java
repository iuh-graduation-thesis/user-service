package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.UpdateKeycloakUserRequest;
import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;

public interface KeycloakUserService {

    void updateUserIfChanged(String userId, UpdateKeycloakUserRequest request);

    void updatePassword(String userId, String username, UpdatePasswordRequest request);
}
