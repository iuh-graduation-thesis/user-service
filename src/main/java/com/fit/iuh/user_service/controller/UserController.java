package com.fit.iuh.user_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fit.iuh.user_service.dto.base.ApiResponse;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
import com.fit.iuh.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PutMapping("/me/onboarding")
    public ApiResponse<Void> onboarding(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid OnboardingRequest request) {
        userService.processOnboarding(jwt.getSubject(), request);

        return ApiResponse.<Void>builder()
                .message("Onboarding completed")
                .build();
    }
}
