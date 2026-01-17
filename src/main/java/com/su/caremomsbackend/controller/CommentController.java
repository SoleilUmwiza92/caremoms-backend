package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.CreateCommentRequest;
import com.su.caremomsbackend.model.Comment;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.CommentService;
import com.su.caremomsbackend.service.PostService;
import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserSyncService userSyncService;
    private final TokenValidationService tokenValidationService;

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> add(@PathVariable Long postId,
                                       @RequestBody CreateCommentRequest dto,
                                       HttpServletRequest request) {
        // If user is NOT logged in yet → allow frontend to continue silently
        if (!tokenValidationService.validateToken(request)) {
            log.error("Authorization header is null in AuthSync");
            return ResponseEntity.status(401).build();
        }
        String token= request.getHeader("Authorization").substring(7);
        User u = userSyncService.getUserDetails(token);
        Post p = postService.require(postId);
        return ResponseEntity.ok(commentService.add(dto, u, p));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> list(@PathVariable Long postId) {
        Post p = postService.require(postId);
        return ResponseEntity.ok(commentService.find(p));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id,
//                                       @AuthenticationPrincipal Jwt jwt) {
//        User u = userSyncService.getOrCreate(jwt);
//        commentService.deleteIfOwner(id, u);
//        return ResponseEntity.noContent().build();
//    }
}
