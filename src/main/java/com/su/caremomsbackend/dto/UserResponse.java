package com.su.caremomsbackend.dto;

import com.su.caremomsbackend.model.User;

public record UserResponse(
        Long id,
        String supabaseId,
        String email,
        String UserName
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getSupabaseId(),
                user.getEmail(),
                user.getUserName()
        );
    }
}