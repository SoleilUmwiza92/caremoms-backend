package com.su.caremomsbackend.controller;

import com.su.caremomsbackend.dto.SendMessageRequest;
import com.su.caremomsbackend.model.Message;
import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.MessageService;
import com.su.caremomsbackend.service.TokenValidationService;
import com.su.caremomsbackend.service.UserSyncService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final UserSyncService userSyncService;
    private final TokenValidationService tokenValidationService;

    @PostMapping
    public ResponseEntity<Message> send(@RequestBody SendMessageRequest dto,
                                        HttpServletRequest request) {
        // If user is NOT logged in yet → allow frontend to continue silently
        if (!tokenValidationService.validateToken(request) && tokenValidationService.getAdminUser(request.getHeader("Admin")) == null) {
            log.error("Authentication validation failed");
            return ResponseEntity.status(401).build();
        }

        User user=null;
        if(request.getHeader("Authorization") !=null){
            String token= request.getHeader("Authorization").substring(7);
            user = userSyncService.getUserDetails(token);
        }else{
            user = tokenValidationService.getAdminUser(request.getHeader("Admin"));
        }

        log.info("User {} calling {}",user.getUserName(), dto.getContent());
        return ResponseEntity.ok(messageService.send(dto, user));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Message>> list(@PathVariable String roomId,
                                              @RequestParam(defaultValue = "25") int limit) {

        return ResponseEntity.ok(messageService.get(roomId, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       HttpServletRequest request) {
        if (!tokenValidationService.validateToken(request)) {
            log.error("Authentication validation failed for deletion");
            return ResponseEntity.status(401).build();
        }
        String token= request.getHeader("Authorization").substring(7);
        User u = userSyncService.getUserDetails(token);
        messageService.deleteIfOwner(id, u);
        return ResponseEntity.noContent().build();
    }
}
