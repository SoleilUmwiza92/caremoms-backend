package com.su.caremomsbackend.dto;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String content;
    private String metadata;
}
