package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.OnboardingRequest;

public interface KeycloakUserService {

    void updateNameIfChanged(String keycloakId, OnboardingRequest onboardingRequest);
}
