package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.CreateCommentRequest;
import com.su.caremomsbackend.model.Comment;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.CommentService;
import com.su.caremomsbackend.service.PostService;
import com.su.caremomsbackend.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserSyncService userSyncService;

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> add(@PathVariable Long postId,
                                       @RequestBody CreateCommentRequest dto,
                                       @AuthenticationPrincipal Jwt jwt) {
        User u = userSyncService.getOrCreate(jwt);
        Post p = postService.require(postId);
        return ResponseEntity.ok(commentService.add(dto, u, p));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> list(@PathVariable Long postId) {
        Post p = postService.require(postId);
        return ResponseEntity.ok(commentService.find(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal Jwt jwt) {
        User u = userSyncService.getOrCreate(jwt);
        commentService.deleteIfOwner(id, u);
        return ResponseEntity.noContent().build();
    }
}
