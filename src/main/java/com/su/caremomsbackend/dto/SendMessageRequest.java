package com.su.caremomsbackend.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String roomId;
    private String content;
}
