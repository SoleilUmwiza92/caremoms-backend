package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.SendMessageRequest;
import com.su.caremomsbackend.model.Message;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.MessageService;
import com.su.caremomsbackend.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserSyncService userSyncService;

    @PostMapping
    public ResponseEntity<Message> send(@RequestBody SendMessageRequest dto,
                                        @AuthenticationPrincipal Jwt jwt) {

        User u = userSyncService.getOrCreate(jwt);
        return ResponseEntity.ok(messageService.send(dto, u));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Message>> list(@PathVariable String roomId,
                                              @RequestParam(defaultValue = "25") int limit) {

        return ResponseEntity.ok(messageService.get(roomId, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal Jwt jwt) {

        User u = userSyncService.getOrCreate(jwt);
        messageService.deleteIfOwner(id, u);
        return ResponseEntity.noContent().build();
    }
}
