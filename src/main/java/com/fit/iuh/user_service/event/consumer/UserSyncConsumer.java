package com.fit.iuh.user_service.event.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fit.iuh.user_service.constant.RoleConstants;
import com.fit.iuh.user_service.filter.UserContextHolder;
import com.fit.iuh.user_service.model.Role;
import com.fit.iuh.user_service.model.User;
import com.fit.iuh.user_service.repository.RoleRepository;
import com.fit.iuh.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSyncConsumer {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.keycloak-user-events}")
    public void userSyncListener(String msg) {
        try {
            JsonNode payload = objectMapper.readTree(msg);
            String action = payload.path("action").asText("UNKNOWN");
            String source = payload.path("source").asText("UNKNOWN");
            String userId = payload.path("userId").asText();

            log.info("[KAFKA] [{}] Source: {} | UserID: {} | Email: {}",
                    String.format("%-6s", action), String.format("%-5s", source),
                    userId, payload.path("email").asText());

            try {
                UserContextHolder.set(UserContextHolder.builder()
                        .keycloakId(userId)
                        .email(payload.path("email").asText(null))
                        .build());

                switch (action) {
                    case "CREATE" -> handleCreateUser(payload, userId);
                    case "DELETE" -> handleDeleteUser(userId);
                    case "UPDATE" -> handleUpdateUser(payload, userId);
                    default -> log.warn("Unrecognized action: {}", action);
                }
            } finally {
                UserContextHolder.clear();
            }

        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", e.getMessage());
        }
    }

    private void handleCreateUser(JsonNode payload, String keycloakId) {
        try {
            if (userRepository.existsById(keycloakId)) {
                log.warn("User {} already exists. Skipping.", keycloakId);
                return;
            }

            User newUser = new User();
            newUser.setId(keycloakId);
            newUser.setUsername(payload.path("username").asText(keycloakId));
            newUser.setEmail(payload.path("email").asText(null));
            newUser.setFirstName(payload.path("firstName").asText(null));
            newUser.setLastName(payload.path("lastName").asText(null));
            if (payload.hasNonNull("enabled")) newUser.setActive(payload.path("enabled").asBoolean());
            if (payload.hasNonNull("emailVerified")) newUser.setEmailVerified(payload.path("emailVerified").asBoolean());
            newUser.setRole(resolveDefaultRole());
            userRepository.save(newUser);

            log.info("Successfully persisted new user to DB.");
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage());
            throw e;
        }
    }

    private void handleUpdateUser(JsonNode payload, String keycloakId) {
        userRepository.findById(keycloakId).ifPresentOrElse(user -> {
            if (payload.has("username")) user.setUsername(payload.path("username").asText(null));
            if (payload.has("email")) user.setEmail(payload.path("email").asText(null));
            if (payload.has("firstName")) user.setFirstName(payload.path("firstName").asText(null));
            if (payload.has("lastName")) user.setLastName(payload.path("lastName").asText(null));

            if (payload.hasNonNull("enabled")) {
                user.setActive(payload.path("enabled").asBoolean());
            }
            if (payload.hasNonNull("emailVerified")) {
                user.setEmailVerified(payload.path("emailVerified").asBoolean());
            }

            userRepository.save(user);
            log.info("Successfully updated user {} from Kafka event.", keycloakId);
        }, () -> log.warn("User {} not found. Skipping update.", keycloakId));
    }

    private void handleDeleteUser(String keycloakId) {
        if (!userRepository.existsById(keycloakId)) {
            log.warn("User {} not found. Skipping delete.", keycloakId);
            return;
        }

        userRepository.deleteById(keycloakId);
        log.info("Successfully deleted user {} from DB.", keycloakId);
    }

    private Role resolveDefaultRole() {
        return roleRepository
                .findByName(RoleConstants.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Missing default role: " + RoleConstants.CUSTOMER));
    }
}
