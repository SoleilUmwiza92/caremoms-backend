package com.su.caremomsbackend.service;

import com.su.caremomsbackend.dto.CreateCommentRequest;
import com.su.caremomsbackend.model.Comment;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repo;

    public Comment add(CreateCommentRequest dto, User user, Post post) {
        Comment c = new Comment();
        c.setUser(user);
        c.setPost(post);
        c.setMessage(dto.getMessage());
        c.setCreatedAt(Instant.now());
        return repo.save(c);
    }

    public List<Comment> find(Post post) {
        return repo.findByPostOrderByCreatedAtAsc(post);
    }

    public void deleteIfOwner(Long id, User user) {
        Comment c = repo.findById(id).orElseThrow();
        if (!c.getUser().getId().equals(user.getId())) throw new RuntimeException("Forbidden");
        repo.delete(c);
    }
}
