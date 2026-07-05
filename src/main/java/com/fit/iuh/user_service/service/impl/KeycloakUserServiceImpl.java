package com.fit.iuh.user_service.service.impl;

import java.util.Objects;

import jakarta.ws.rs.WebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.config.security.KeycloakPasswordGrantClientFactory;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.dto.request.UpdatePasswordRequest;
import com.fit.iuh.user_service.service.KeycloakUserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {

    RealmResource keycloakRealm;
    KeycloakPasswordGrantClientFactory keycloakPasswordGrantClientFactory;

    @Override
    public void updateNameIfChanged(String userId, OnboardingRequest onboardingRequest) {
        try {
            var userResource = keycloakRealm.users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();

            if (!isNameChanged(userRepresentation, onboardingRequest)) {
                log.info("Keycloak name is unchanged for user {}", userId);
                return;
            }

            userRepresentation.setFirstName(onboardingRequest.firstName());
            userRepresentation.setLastName(onboardingRequest.lastName());

            userResource.update(userRepresentation);
            log.info("Requested Keycloak name update for user {}", userId);
        } catch (WebApplicationException exception) {
            log.error("Failed to update Keycloak user {}: {}", userId, exception.getMessage());
            throw new AppException(ErrorCode.KEYCLOAK_USER_UPDATE_FAILED);
        }
    }

    @Override
    public void updatePassword(String userId, String username, UpdatePasswordRequest request) {
        verifyOldPassword(username, request.oldPassword());

        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.newPassword());
            credential.setTemporary(false);

            keycloakRealm
                    .users()
                    .get(userId)
                    .resetPassword(credential);

            log.info("Updated Keycloak password for user {}", userId);
        } catch (WebApplicationException exception) {
            log.error("Failed to update Keycloak password for user {}: {}", userId, exception.getMessage());
            throw new AppException(ErrorCode.KEYCLOAK_PASSWORD_UPDATE_FAILED);
        }
    }

    private void verifyOldPassword(String username, String oldPassword) {
        try (Keycloak keycloak = keycloakPasswordGrantClientFactory.createPasswordGrantClient(username, oldPassword)) {
            keycloak.tokenManager().getAccessToken();
        } catch (WebApplicationException exception) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }
    }

    private boolean isNameChanged(UserRepresentation userRepresentation, OnboardingRequest onboardingRequest) {
        return !Objects.equals(userRepresentation.getFirstName(), onboardingRequest.firstName())
                || !Objects.equals(userRepresentation.getLastName(), onboardingRequest.lastName());
    }
}
