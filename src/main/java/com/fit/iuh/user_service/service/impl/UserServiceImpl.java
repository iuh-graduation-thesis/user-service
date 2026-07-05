package com.fit.iuh.user_service.service.impl;

import java.util.Objects;
import java.util.Collections;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.response.UserPermissionsResponse;
import com.fit.iuh.user_service.filter.UserContextHolder;
import com.fit.iuh.user_service.model.User;
import com.fit.iuh.user_service.repository.UserRepository;
import com.fit.iuh.user_service.service.UserService;

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
        RealmResource keycloakRealm;

        @Override
        public void processOnboarding(OnboardingRequest onboardingRequest) {
                String email = UserContextHolder.get().getEmail();
                User user = userRepository
                                .findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                if (Boolean.TRUE.equals(user.getOnBoarded())) {
                        throw new AppException(ErrorCode.USER_ALREADY_ONBOARDED);
                }

                updateKeycloakNameIfChanged(user.getId(), onboardingRequest);

                user.setPhone(onboardingRequest.phone());
                user.setDob(onboardingRequest.dob());
                user.setGender(onboardingRequest.gender());
                user.setAvatarUrl(onboardingRequest.avatarUrl());
                user.setOnBoarded(true);

                userRepository.save(user);
                log.info("Onboarding completed for user {}", user.getId());
        }

        @Override
        public UserPermissionsResponse getUserPermissions(String keycloakId) {
                User user = userRepository.findById(keycloakId)
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

        private void updateKeycloakNameIfChanged(String keycloakId, OnboardingRequest onboardingRequest) {
                try {
                        var userResource = keycloakRealm.users().get(keycloakId);
                        UserRepresentation userRepresentation = userResource.toRepresentation();

                        if (!isNameChanged(userRepresentation, onboardingRequest)) {
                                log.info("Keycloak name is unchanged for user {}", keycloakId);
                                return;
                        }

                        userRepresentation.setFirstName(onboardingRequest.firstName());
                        userRepresentation.setLastName(onboardingRequest.lastName());

                        userResource.update(userRepresentation);
                        log.info("Requested Keycloak name update for user {}", keycloakId);
                } catch (WebApplicationException exception) {
                        log.error("Failed to update Keycloak user {}: {}", keycloakId, exception.getMessage());
                        throw new AppException(ErrorCode.KEYCLOAK_USER_UPDATE_FAILED);
                }
        }

        private boolean isNameChanged(UserRepresentation userRepresentation, OnboardingRequest onboardingRequest) {
                return !Objects.equals(userRepresentation.getFirstName(), onboardingRequest.firstName())
                                || !Objects.equals(userRepresentation.getLastName(), onboardingRequest.lastName());
        }

}
