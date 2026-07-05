package com.fit.iuh.user_service.service.impl;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.request.UpdateAvatarRequest;
import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;
import com.fit.iuh.user_service.dto.request.UpdateProfileRequest;
import com.fit.iuh.user_service.dto.response.UserPermissionsResponse;
import com.fit.iuh.user_service.dto.response.UserProfileResponse;
import com.fit.iuh.user_service.mapper.UserMapper;
import com.fit.iuh.user_service.model.User;
import com.fit.iuh.user_service.repository.UserRepository;
import com.fit.iuh.user_service.service.KeycloakUserService;
import com.fit.iuh.user_service.service.UserService;
import com.fit.iuh.user_service.utils.CurrentUserUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

        UserRepository userRepository;
        KeycloakUserService keycloakUserService;
        CurrentUserUtils currentUserUtils;

        @Override
        public void processOnboarding(OnboardingRequest onboardingRequest) {
                User user = currentUserUtils.getCurrentUser();

                if (Boolean.TRUE.equals(user.getOnBoarded())) {
                        throw new AppException(ErrorCode.USER_ALREADY_ONBOARDED);
                }

                keycloakUserService.updateNameIfChanged(
                                user.getId(),
                                onboardingRequest.firstName(),
                                onboardingRequest.lastName());

                user.setPhone(onboardingRequest.phone());
                user.setDob(onboardingRequest.dob());
                user.setGender(onboardingRequest.gender());
                user.setOnBoarded(true);

                userRepository.save(user);
                log.info("Onboarding completed for user {}", user.getId());
        }

        @Override
        public UserPermissionsResponse getUserPermissions(String userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                var role = user.getRole();
                var permissions = role == null || role.getPermissions() == null
                                ? Collections.<String>emptySet()
                                : role.getPermissions().stream()
                                                .map(permission -> permission.getName())
                                                .collect(Collectors.toSet());

                return UserPermissionsResponse.builder()
                                .email(user.getEmail())
                                .role(role != null ? role.getName() : null)
                                .permissions(permissions)
                                .build();
        }

        @Override
        public void updateUserPassword(UpdatePasswordRequest request) {
                String userId = currentUserUtils.getCurrentUserId();
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                keycloakUserService.updatePassword(userId, user.getUsername(), request);
        }

        @Override
        @Transactional(readOnly = true)
        public UserProfileResponse getUserProfile() {
                return UserMapper.mapToUserProfileResponse(currentUserUtils.getCurrentUser());
        }

        @Override
        public void updateUserProfile(UpdateProfileRequest request) {
                User user = currentUserUtils.getCurrentUser();

                keycloakUserService.updateNameIfChanged(
                                user.getId(),
                                request.firstName(),
                                request.lastName());

                if (request.phone() != null) {
                        user.setPhone(request.phone());
                }
                if (request.dob() != null) {
                        user.setDob(request.dob());
                }
                if (request.gender() != null) {
                        user.setGender(request.gender());
                }

                userRepository.save(user);
                log.info("Updated profile for user {}", user.getId());
        }

        @Override
        public void updateUserAvatar(UpdateAvatarRequest request) {
                // TODO Auto-generated method stub
        }

        
}
