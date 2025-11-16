package com.su.careMoms_be_app.controller;

import com.su.careMoms_be_app.model.ChatMessage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {

    @Test
    void sendMessage_shouldSetTimestampAndReturnSameContent() {
        // Arrange
        MessageController controller = new MessageController();
        ChatMessage input = new ChatMessage();
        input.setNickname("test-user");
        input.setContent("Hello CareMoms!");

        // Pre-condition: timestamp should be null
        assertNull(input.getTimestamp(), "Expected initial timestamp to be null");

        // Act
        ChatMessage result = controller.sendMessage(input);

        // Assert: timestamp was set
        assertNotNull(result.getTimestamp(), "Controller must set a timestamp");
        assertTrue(result.getTimestamp() instanceof Date, "Timestamp should be a Date");

        // Assert: content and nickname preserved
        assertEquals("Hello CareMoms!", result.getContent());
        assertEquals("test-user", result.getNickname());
    }

    @Test
    void sendMessage_withEmptyContent_shouldStillReturnMessageWithTimestamp() {
        MessageController controller = new MessageController();
        ChatMessage input = new ChatMessage();
        input.setNickname("bob");
        input.setContent("");

        ChatMessage result = controller.sendMessage(input);

        assertNotNull(result.getTimestamp());
        assertEquals("", result.getContent());
        assertEquals("bob", result.getNickname());
    }
}
