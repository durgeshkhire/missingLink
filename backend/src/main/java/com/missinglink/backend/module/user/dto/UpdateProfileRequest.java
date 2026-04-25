package com.missinglink.backend.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 300, message = "Bio must be under 300 characters")
    private String bio;
}