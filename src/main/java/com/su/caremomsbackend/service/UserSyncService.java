package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserSyncService {
    private final TokenValidationService tokenValidationService;
    private final UserRepository userRepository;

    public User getOrCreate(String token, User user) {
       String supabaseId = tokenValidationService.extractPersonId(token);
        return userRepository.findBySupabaseId(supabaseId)
                .map(currentUser -> {
                    currentUser.setEmail(user.getEmail());
                    currentUser.setUserName(user.getUserName());
                    currentUser.setUpdatedAt(Instant.now());
                    currentUser.setDob(user.getDob());
                    if(currentUser.getRole() == null){
                        currentUser.setRole(user.getRole());
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    log.info("User to be created {}", user.getUserName());
                    user.setSupabaseId(supabaseId);
                    user.setCreatedAt(Instant.now());
                    user.setUpdatedAt(Instant.now());
                    return userRepository.save(user);
                });
    }



    public User updateUser( User newData){
        User currentUser= userRepository.findByEmail(newData.getEmail()).orElse(null);
        if(currentUser != null){
            return userRepository.save(newData);
        }
        return null;
    }

    public User getUserDetails(String token){
        String supabaseId = tokenValidationService.extractPersonId(token);
        User existingUser= userRepository.findBySupabaseId(supabaseId).orElse(null);
        if( existingUser != null){
            return existingUser;
        }
        String email = tokenValidationService.extractEmail(token);
        String userName= tokenValidationService.extractName(token);
        String role= tokenValidationService.extractRole(token);
        User newUser = new User();
        newUser.setRole(role);
        newUser.setSupabaseId(supabaseId);
        newUser.setUserName(userName);
        newUser.setEmail(email);
        return newUser;
    }

    public void removeUserAccount(User user){
        userRepository.delete(user);
    }
}
