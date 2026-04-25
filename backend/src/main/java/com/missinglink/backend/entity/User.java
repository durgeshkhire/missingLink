package com.missinglink.backend.entity;

import com.missinglink.backend.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String bio;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String avatarUrl;

    @Column(precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    private boolean emailVerified =false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    private int totalRatings= 0;

    @PrePersist
    protected void onCreate(){
        createdAt= LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }


    public void setBio(@Size(max = 300, message = "Bio must be under 300 characters") String bio) {

    }
}
