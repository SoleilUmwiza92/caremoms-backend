package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.model.ChatMessage;
import com.su.caremomsbackend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/messages")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        ChatMessage savedMessage = chatService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@RequestParam(required = false) Long afterTimestamp) {
        if (afterTimestamp != null) {
            return ResponseEntity.ok(chatService.getMessagesAfter(afterTimestamp));
        } else {
            return ResponseEntity.ok(chatService.getAllMessages());
        }
    }

    @GetMapping("/messages/room/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessagesByRoom(
            @PathVariable String roomId,
            @RequestParam(required = false) Long afterTimestamp) {

        if (afterTimestamp != null) {
            return ResponseEntity.ok(chatService.getMessagesByRoomAfter(roomId, afterTimestamp));
        } else {
            return ResponseEntity.ok(chatService.getMessagesByRoom(roomId));
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        chatService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
