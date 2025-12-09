package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthSyncController {

    private final UserSyncService userSyncService;

    @PostMapping("/sync")
    public ResponseEntity<?> sync(@AuthenticationPrincipal Jwt jwt) {

        // If user is NOT logged in yet → allow frontend to continue silently
        if (jwt == null) {
            return ResponseEntity.ok().build();
        }

        // Only sync when token EXISTS
        return ResponseEntity.ok(userSyncService.getOrCreate(jwt));
    }
}
