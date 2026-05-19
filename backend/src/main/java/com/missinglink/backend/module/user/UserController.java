package com.missinglink.backend.module.user;

import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.user.dto.ChangePasswordRequest;
import com.missinglink.backend.module.user.dto.UpdateProfileRequest;
import com.missinglink.backend.module.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users/me — get my profile
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile() {
        UserProfileResponse response = userService.getMyProfile();
        return ResponseEntity.ok(
                ApiResponse.success(response, "Profile fetched successfully"));
    }

    // GET /api/users/{id} — get any user's public profile
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
            @PathVariable UUID id) {
        UserProfileResponse response = userService.getUserProfile(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Profile fetched successfully"));
    }

    // PUT /api/users/me — update my profile
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = userService.updateProfile(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Profile updated successfully"));
    }



    // DELETE /api/users/me — delete my account
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<String>> deleteAccount() {
        String message = userService.deleteAccount();
        return ResponseEntity.ok(
                ApiResponse.success(message, "Account deleted successfully"));
    }
}
