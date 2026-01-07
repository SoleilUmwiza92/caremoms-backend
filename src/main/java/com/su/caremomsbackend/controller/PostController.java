package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.CreatePostRequest;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.PostService;
import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserSyncService userSyncService;
    private final TokenValidationService tokenValidationService;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody CreatePostRequest dto,
                                       HttpServletRequest request) {

        // If user is NOT logged in yet → allow frontend to continue silently
        if (!tokenValidationService.validateToken(request)) {
            return ResponseEntity.status(401).build();
        }
        String token= request.getHeader("Authorization").substring(7);
        User u = userSyncService.getOrCreate(token);
        return ResponseEntity.ok(postService.create(dto, u));
    }

//    @GetMapping("/me")
//    public ResponseEntity<List<Post>> mine(@AuthenticationPrincipal Jwt jwt) {
//        User u = userSyncService.getOrCreate(jwt);
//        return ResponseEntity.ok(postService.myPosts(u));
//    }
}
