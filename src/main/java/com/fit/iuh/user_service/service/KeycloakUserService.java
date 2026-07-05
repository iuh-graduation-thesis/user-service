package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;

public interface KeycloakUserService {

    void updateNameIfChanged(String userId, OnboardingRequest onboardingRequest);

    void updatePassword(String userId, String username, UpdatePasswordRequest request);
}
