package com.app.ChatService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDTO {
    Long index;
    String sender;
    String content;
    Instant time;
}
