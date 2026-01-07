package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.UserResponse;
import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.error("Token is valid");
        return ResponseEntity.ok(UserResponse.fromEntity(userSyncService.getUserDetails(token)));
    }


//    @PutMapping("/me")
//    public ResponseEntity<User> update(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody User update
//    ) {
//        String token= authHeader.substring(7);
//
//        if (!tokenValidationService.validateToken(token)) {
//            log.error("Update failed, Authentication validation failed");
//            return ResponseEntity.status(401).build();
//        }
//
//        return userRepository.findBySupabaseId(jwt.getSubject())
//                .map(user -> {
//                    user.setDisplayName(update.getDisplayName());
//                    return ResponseEntity.ok(userRepository.save(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
}
