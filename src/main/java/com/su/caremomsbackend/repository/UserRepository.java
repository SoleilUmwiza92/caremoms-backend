package com.su.caremomsbackend.repository;

import com.su.caremomsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySupabaseId(String supabaseId);
    Optional<User> findByEmail(String email);
}
