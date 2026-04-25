package com.missinglink.backend.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpReq {
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;
}
