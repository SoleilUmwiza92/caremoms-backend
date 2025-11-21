package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final List<ChatMessage> messages = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ChatMessage saveMessage(ChatMessage message) {
        message.setId(idGenerator.getAndIncrement());
        message.setTimestamp(System.currentTimeMillis());
        messages.add(message);
        return message;
    }

    public List<ChatMessage> getAllMessages() {
        return new ArrayList<>(messages);
    }

    public List<ChatMessage> getMessagesAfter(Long afterTimestamp) {
        return messages.stream()
                .filter(m -> m.getTimestamp() > afterTimestamp)
                .collect(Collectors.toList());
    }

    public List<ChatMessage> getMessagesByRoom(String roomId) {
        return messages.stream()
                .filter(m -> m.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    public List<ChatMessage> getMessagesByRoomAfter(String roomId, Long afterTimestamp) {
        return messages.stream()
                .filter(m -> m.getRoomId().equals(roomId) && m.getTimestamp() > afterTimestamp)
                .collect(Collectors.toList());
    }

    public void deleteMessage(Long messageId) {
        messages.removeIf(m -> m.getId().equals(messageId));
    }
}
