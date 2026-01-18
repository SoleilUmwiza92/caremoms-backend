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
        if(request.getHeader("Authorization") !=null){
            String token= request.getHeader("Authorization").substring(7);
            return ResponseEntity.ok(UserResponse.fromEntity(userSyncService.getUserDetails(token)));
        }else if(request.getHeader("Admin") !=null){
            return ResponseEntity.ok(UserResponse.fromEntity(tokenValidationService.getAdminUser(request.getHeader("Admin"))));
        }
        return ResponseEntity.notFound().build();
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

        if (!tokenValidationService.validateToken(request)) {
            log.error("Update failed, Authentication validation failed");
            return ResponseEntity.status(401).build();
        }

        User updatedUser=userSyncService.updateUser(update);
        if(updatedUser != null){
            return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> delete(HttpServletRequest request){
        if (!tokenValidationService.validateToken(request)) {
            log.error("Delete user account failed, Authentication validation failed");
            return ResponseEntity.status(401).build();
        }
        String token= request.getHeader("Authorization").substring(7);
        User user = userSyncService.getUserDetails(token);
        if(user != null){
            userSyncService.removeUserAccount(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
