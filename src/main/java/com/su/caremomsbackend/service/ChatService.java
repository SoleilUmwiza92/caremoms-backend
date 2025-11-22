package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class ChatService {

    // Thread-safe in-memory storage
    private final List<ChatMessage> messages = new CopyOnWriteArrayList<>();

    // Save a new message
    public ChatMessage saveMessage(ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(new Date());
        }
        messages.add(message);
        return message;
    }

    // Get all messages sorted by timestamp
    public List<ChatMessage> getAllMessages() {
        return messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());
    }

    // Get messages after a certain timestamp (for long-polling)
    public List<ChatMessage> getMessagesAfter(Date timestamp) {
        return messages.stream()
                .filter(msg -> msg.getTimestamp().after(timestamp))
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());
    }

    // Delete a message uniquely identified by (nickname + timestamp)
    public void deleteMessage(String nickname, Date timestamp) {
        messages.removeIf(msg ->
                msg.getNickname().equals(nickname) &&
                        msg.getTimestamp().equals(timestamp)
        );
    }

    // Get the latest timestamp (for polling initialization)
    public Date getLatestTimestamp() {
        return messages.stream()
                .map(ChatMessage::getTimestamp)
                .max(Date::compareTo)
                .orElse(new Date(0));
    }

    // Clear all messages
    public void clearAllMessages() {
        messages.clear();
    }
}
