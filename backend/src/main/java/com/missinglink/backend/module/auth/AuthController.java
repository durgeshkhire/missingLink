package com.missinglink.backend.module.auth;

import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/send-motp")
//    public ResponseEntity<ApiResponse<String>> sendRegisterOtp(
//            @Valid @RequestBody SendLoginOtpRequest request) {
//        String message = authService.sendRegisterOtp(request.getPhone());
//        return ResponseEntity.ok(
//                ApiResponse.success(message, "OTP sent successfully"));
//    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterReq req){
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginReq loginReq){
        return authService.login(loginReq);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @Valid @RequestBody SendOtpReq request) {
        String message = authService.sendOtp(request.getEmail());
        return ResponseEntity.ok(
                ApiResponse.success(message, "OTP sent successfully"));
    }

    // Step 2 — Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        String message = authService.verifyOtp(request);
        return ResponseEntity.ok(
                ApiResponse.success(message, "OTP verified successfully"));
    }

}
