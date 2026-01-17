package com.su.caremomsbackend.dto;

import com.su.caremomsbackend.model.User;

public record UserResponse(
        Long id,
        String supabaseId,
        String email,
        String userName,
        String role,
        String dob
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getSupabaseId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole(),
                user.getDob()
        );
    }
}