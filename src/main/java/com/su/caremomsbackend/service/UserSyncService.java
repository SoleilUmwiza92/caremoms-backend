package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSyncService {
    private final TokenValidationService tokenValidationService;
    private final UserRepository userRepository;

    public User getOrCreate(String token) {
      String supabaseId = tokenValidationService.extractPersonId(token);
       String email = tokenValidationService.extractEmail(token);
       String name = tokenValidationService.extractName(token);
      // String role= tokenValidationService.extractRole(token);
        return userRepository.findBySupabaseId(supabaseId)
                .map(user -> {
                    user.setEmail(email);
                    user.setUserName(name);
                  //  user.setRole(role);
                    user.setUpdatedAt(Instant.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setSupabaseId(supabaseId);
                    newUser.setUserName(name);
                    newUser.setEmail(email);
                    newUser.setCreatedAt(Instant.now());
                    newUser.setUpdatedAt(Instant.now());
                    return userRepository.save(newUser);
                });
    }

    public User getUserDetails(String token){
        String supabaseId = tokenValidationService.extractPersonId(token);
        return userRepository.findBySupabaseId(supabaseId).orElse(null);
    }
}
