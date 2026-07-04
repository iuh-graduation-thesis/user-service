package com.fit.iuh.user_service.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.dto.request.OnboardingRequest;
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

    @Override
    public void processOnboarding(String userId, OnboardingRequest onboardingRequest) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setFirstName(onboardingRequest.firstName());
        user.setLastName(onboardingRequest.lastName());
        user.setPhone(onboardingRequest.phone());
        user.setDob(onboardingRequest.dob());
        user.setGender(onboardingRequest.gender());
        user.setAvatarUrl(onboardingRequest.avatarUrl());
        user.setOnBoarded(true);

        userRepository.save(user);
        log.info("Onboarding completed for user {}", user.getId());
    }
    
}
