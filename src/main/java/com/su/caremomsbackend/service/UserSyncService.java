package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSyncService {

    private final UserRepository userRepository;

    public User getOrCreate(Jwt jwt) {

        String supabaseId = jwt.getSubject();
        String email = jwt.getClaim("email");

        return userRepository.findBySupabaseId(supabaseId)
                .map(user -> {
                    user.setEmail(email);
                    user.setUpdatedAt(Instant.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setSupabaseId(supabaseId);
                    newUser.setEmail(email);
                    newUser.setCreatedAt(Instant.now());
                    newUser.setUpdatedAt(Instant.now());
                    return userRepository.save(newUser);
                });
    }
}
