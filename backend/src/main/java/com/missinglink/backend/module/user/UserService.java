package com.missinglink.backend.module.user;


import com.missinglink.backend.entity.User;
import com.missinglink.backend.module.user.dto.ChangePasswordRequest;
import com.missinglink.backend.module.user.dto.UpdateProfileRequest;
import com.missinglink.backend.module.user.dto.UserProfileResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Get currently logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get my profile
    public UserProfileResponse getMyProfile() {
        return mapToResponse(getCurrentUser());
    }

    // Get any user's public profile by ID
    public UserProfileResponse getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    // Update my profile
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.setName(request.getName());
        user.setBio(request.getBio());
        userRepository.save(user);
        return mapToResponse(user);
    }

    // Change password
    @Transactional
    public String changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Prevent same password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

    // Delete my account
    @Transactional
    public String deleteAccount() {
        User user = getCurrentUser();
        userRepository.delete(user);
        return "Account deleted successfully";
    }

    // Map entity to response
    private UserProfileResponse mapToResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .emailVerified(user.isEmailVerified())
                .averageRating(user.getAverageRating())
                .totalRatings(user.getTotalRatings())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
