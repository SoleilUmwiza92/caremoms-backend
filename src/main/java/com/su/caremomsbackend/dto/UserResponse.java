package com.su.caremomsbackend.dto;

import com.su.caremomsbackend.model.User;
import lombok.Data;

public record UserResponse(
        Long id,
        String supabaseId,
        String email,
        String displayName
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getSupabaseId(),
                user.getEmail(),
                user.getDisplayName()
        );
    }
}