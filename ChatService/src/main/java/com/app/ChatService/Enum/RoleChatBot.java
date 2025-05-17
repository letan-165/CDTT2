package com.app.ChatService.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleChatBot {
    USER("user"),
    ASSISTANT("assistant")
    ;

    String role;
}
