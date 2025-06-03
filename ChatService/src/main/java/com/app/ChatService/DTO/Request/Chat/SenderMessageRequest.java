package com.app.ChatService.DTO.Request.Chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SenderMessageRequest {
    String receiver;
    String content;
}
