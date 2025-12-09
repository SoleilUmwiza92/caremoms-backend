package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.CreatePostRequest;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.PostService;
import com.su.caremomsbackend.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserSyncService userSyncService;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody CreatePostRequest dto,
                                       @AuthenticationPrincipal Jwt jwt) {

        User u = userSyncService.getOrCreate(jwt);
        return ResponseEntity.ok(postService.create(dto, u));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> mine(@AuthenticationPrincipal Jwt jwt) {
        User u = userSyncService.getOrCreate(jwt);
        return ResponseEntity.ok(postService.myPosts(u));
    }
}
