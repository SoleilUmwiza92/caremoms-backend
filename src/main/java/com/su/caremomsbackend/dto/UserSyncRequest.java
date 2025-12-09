package com.su.caremomsbackend.dto;

import lombok.Data;

@Data
public class UserSyncRequest {
    private String supabaseId;
    private String email;
    private String displayName;
}
