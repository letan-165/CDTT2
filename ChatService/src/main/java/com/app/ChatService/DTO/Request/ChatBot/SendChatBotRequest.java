package com.app.ChatService.DTO.Request.ChatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendChatBotRequest {
    String content;
}
