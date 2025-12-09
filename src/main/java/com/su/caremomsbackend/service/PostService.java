package com.su.caremomsbackend.service;

import com.su.caremomsbackend.dto.CreatePostRequest;
import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repo;

    public Post create(CreatePostRequest dto, User user) {
        Post p = new Post();
        p.setUser(user);
        p.setContent(dto.getContent());
        p.setMetadata(dto.getMetadata());
        p.setCreatedAt(Instant.now());
        return repo.save(p);
    }

    public List<Post> myPosts(User u) {
        return repo.findByUserOrderByCreatedAtDesc(u);
    }

    public Post require(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
