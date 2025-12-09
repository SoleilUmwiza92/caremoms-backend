package com.su.caremomsbackend.repository;

import com.su.caremomsbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderByCreatedAtDesc(String roomId);
}
