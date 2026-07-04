package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.response.UserPermissionsResponse;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    void processOnboarding(OnboardingRequest onboardingRequest);

    @Transactional(readOnly = true)
    UserPermissionsResponse getUserPermissions(String keycloakId);
}
