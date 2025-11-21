package com.su.caremomsbackend.model;

public class ChatMessage {
    private Long id;
    private String roomId;
    private String sender;
    private String content;
    private Long timestamp; // store as epoch millis

    public ChatMessage() {}

    public ChatMessage(Long id, String roomId, String sender, String content, Long timestamp) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
