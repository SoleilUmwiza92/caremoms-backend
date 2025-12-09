package com.su.caremomsbackend.service;

import com.su.caremomsbackend.dto.SendMessageRequest;
import com.su.caremomsbackend.model.Message;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;

    public Message send(SendMessageRequest dto, User sender) {
        Message m = new Message();
        m.setSender(sender);
        m.setRoomId(dto.getRoomId());
        m.setContent(dto.getContent());
        m.setCreatedAt(Instant.now());
        return repo.save(m);
    }

    public List<Message> get(String roomId, int limit) {
        return repo.findByRoomIdOrderByCreatedAtDesc(roomId)
                .stream()
                .limit(limit)
                .toList();
    }

    public void deleteIfOwner(Long id, User user) {
        Message m = repo.findById(id).orElseThrow();
        if (!m.getSender().getId().equals(user.getId())) throw new RuntimeException("Forbidden");
        repo.delete(m);
    }
}
