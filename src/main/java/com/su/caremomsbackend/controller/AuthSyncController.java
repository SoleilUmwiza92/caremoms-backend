package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthSyncController {

    private final UserSyncService userSyncService;
    private final TokenValidationService tokenValidationService;



    @PostMapping("/validate")
    public ResponseEntity<?> sync(HttpServletRequest request) {

        // If user is NOT logged in yet → allow frontend to continue silently
        if (!tokenValidationService.validateToken(request)) {
            log.error("Authentication validation failed");
            return ResponseEntity.status(401).build();
        }
        String token= request.getHeader("Authorization").substring(7);
        // Only sync when token is validated successfully
        return ResponseEntity.ok(userSyncService.getUserDetails(token));
    }
}
