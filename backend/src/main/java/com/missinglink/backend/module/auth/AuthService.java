package com.missinglink.backend.module.auth;

import com.missinglink.backend.common.util.EmailService;
import com.missinglink.backend.common.util.JwtUtil;
import com.missinglink.backend.common.util.OtpStore;
import com.missinglink.backend.common.util.SmsService;
import com.missinglink.backend.entity.User;
import com.missinglink.backend.module.auth.dto.AuthResponse;
import com.missinglink.backend.module.auth.dto.LoginReq;
import com.missinglink.backend.module.auth.dto.RegisterReq;
import com.missinglink.backend.module.auth.dto.VerifyOtpRequest;
import com.missinglink.backend.module.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpStore otpStore;
//    private final SmsService smsService;

//    private static final String OTP_TEMPLATE_ID = "1707167368554415373";
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

//    public String sendRegisterOtp(String phone) {
//        if (userRepository.existsByPhone(phone)) {
//            throw new RuntimeException("Phone number already registered");
//        }
//        String otp = generateOtp();
//        String msg = "Your confidential one time password for CarUdyog authentication is "
//                + otp + ". Kindly enter this OTP as prompted.";
//
//
//        otpStore.save(phone, otp);
//
//        int result = smsService.SendUVOTP(phone, msg, OTP_TEMPLATE_ID);
//        if (result == -1) {
//            throw new RuntimeException("Failed to send OTP. Please try again.");
//        }
//        return "OTP sent to " + phone;
//    }

    public AuthResponse register(RegisterReq req){
        if(userRepository.existsByEmail(req.getEmail())){
            throw new RuntimeException("Email already registered!");
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .emailVerified(true)
                .build();
        userRepository.save(user);

        String token= jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());

    }


    public AuthResponse login(LoginReq loginReq) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));

        User user = userRepository.findByEmail(loginReq.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }

    public String sendOtp(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        String otp = generateOtp();
        otpStore.save(email, otp);
        emailService.sendOtp(email, otp);
        return "OTP sent to " + email;
    }

    public String verifyOtp(VerifyOtpRequest request) {
        if (!otpStore.verify(request.getEmail(), request.getOtp())) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        return "Email verified. You can now complete registration.";
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
