package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.model.ChatMessage;
import com.su.caremomsbackend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Get all messages
    @GetMapping
    public List<ChatMessage> getMessages() {
        return chatService.getAllMessages();
    }

    // Get messages after provided timestamp
    @GetMapping("/after")
    public List<ChatMessage> getMessagesAfter(
            @RequestParam("timestamp")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date timestamp
    ) {
        return chatService.getMessagesAfter(timestamp);
    }

    // Send message
    @PostMapping
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        return chatService.saveMessage(message);
    }

    // Delete message by nickname + timestamp
    @DeleteMapping
    public void deleteMessage(
            @RequestParam("nickname") String nickname,
            @RequestParam("timestamp")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date timestamp
    ) {
        chatService.deleteMessage(nickname, timestamp);
    }

    // Get latest timestamp
    @GetMapping("/latest-timestamp")
    public Date latestTimestamp() {
        return chatService.getLatestTimestamp();
    }
}

