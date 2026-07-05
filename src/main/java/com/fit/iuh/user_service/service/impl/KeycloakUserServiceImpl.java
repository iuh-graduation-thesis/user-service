package com.fit.iuh.user_service.service.impl;

import java.util.Objects;

import jakarta.ws.rs.WebApplicationException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
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

    @Override
    public void updateNameIfChanged(String keycloakId, OnboardingRequest onboardingRequest) {
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
