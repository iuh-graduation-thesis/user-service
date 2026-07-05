package com.fit.iuh.user_service.utils;

import org.springframework.stereotype.Component;

import com.fit.iuh.user_service.advice.base.AppException;
import com.fit.iuh.user_service.constant.base.ErrorCode;
import com.fit.iuh.user_service.filter.UserContextHolder;
import com.fit.iuh.user_service.model.User;
import com.fit.iuh.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentUserUtils {

    UserRepository userRepository;

    public String getCurrentUserId() {
        return UserContextHolder.get().getUserId();
    }

    public User getCurrentUser() {
        return userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
