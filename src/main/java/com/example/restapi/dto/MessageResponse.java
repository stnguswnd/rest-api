package com.example.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor


public class MessageResponse {
    private String message;
    private int code;
}
