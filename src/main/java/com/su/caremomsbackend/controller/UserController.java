package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<User> me(@AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) {
            return ResponseEntity.status(401).build();
        }

        return userRepository.findBySupabaseId(jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/me")
    public ResponseEntity<User> update(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody User update
    ) {
        if (jwt == null) {
            return ResponseEntity.status(401).build();
        }

        return userRepository.findBySupabaseId(jwt.getSubject())
                .map(user -> {
                    user.setDisplayName(update.getDisplayName());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
