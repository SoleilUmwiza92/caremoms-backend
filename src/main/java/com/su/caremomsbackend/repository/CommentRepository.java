package com.su.caremomsbackend.repository;

import com.su.caremomsbackend.model.Comment;
import com.su.caremomsbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Used to load comments for a post in created order
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
