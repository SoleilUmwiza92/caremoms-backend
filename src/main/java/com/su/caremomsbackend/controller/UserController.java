package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.UserResponse;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserSyncService userSyncService;
    private final TokenValidationService tokenValidationService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {

        if (!tokenValidationService.validateToken(request)) {
            log.error("Authentication validation failed");
            return ResponseEntity.status(401).build();
        }
        String token=request.getHeader("Authorization").substring(7);
        log.info("Token is valid");
        return ResponseEntity.ok(UserResponse.fromEntity(userSyncService.getUserDetails(token)));
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponse> createUser(HttpServletRequest request, @RequestBody User user) {
        if (!tokenValidationService.validateToken(request)) {
            log.error("Authentication validation failed for add user");
            return ResponseEntity.status(401).build();
        }
        String token=request.getHeader("Authorization").substring(7);
        log.error("Token is valid");
        return ResponseEntity.ok(UserResponse.fromEntity(userSyncService.getOrCreate(token,user)));
    }


    @PutMapping("/me")
    public ResponseEntity<UserResponse> update(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User update,HttpServletRequest request
    ) {
        String token= authHeader.substring(7);

        if (!tokenValidationService.validateToken(request)) {
            log.error("Update failed, Authentication validation failed");
            return ResponseEntity.status(401).build();
        }

        User updatedUser=userSyncService.updateUser(update);
        if(updatedUser != null){
            return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
        }
        return ResponseEntity.status(500).build();

//                userRepository.findBySupabaseId(jwt.getSubject())
//                .map(user -> {
//                    user.setDisplayName(update.getDisplayName());
//                    return ResponseEntity.ok(userRepository.save(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
    }
}
