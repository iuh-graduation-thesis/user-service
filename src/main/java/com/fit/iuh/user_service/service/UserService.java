package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.OnboardingRequest;

public interface UserService {
    void processOnboarding(String userId, OnboardingRequest onboardingRequest);
}
