package com.su.caremomsbackend.repository;

import com.su.caremomsbackend.model.Post;
import com.su.caremomsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(User user);
}
