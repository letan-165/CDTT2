package com.app.ChatService.DTO.Request.Chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecallMessageRequest {
    String chatID;
    int index;
}
