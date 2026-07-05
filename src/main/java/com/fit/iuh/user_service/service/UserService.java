package com.fit.iuh.user_service.service;

import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.request.UpdateAvatarRequest;
import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;
import com.fit.iuh.user_service.dto.request.UpdateProfileRequest;
import com.fit.iuh.user_service.dto.response.UserPermissionsResponse;
import com.fit.iuh.user_service.dto.response.UserProfileResponse;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    void processOnboarding(OnboardingRequest onboardingRequest);

    @Transactional(readOnly = true)
    UserPermissionsResponse getUserPermissions(String userId);

    void updateUserPassword(UpdatePasswordRequest request);

    UserProfileResponse getUserProfile();

    void updateUserProfile(UpdateProfileRequest request);

    void updateUserAvatar(UpdateAvatarRequest request);
}
