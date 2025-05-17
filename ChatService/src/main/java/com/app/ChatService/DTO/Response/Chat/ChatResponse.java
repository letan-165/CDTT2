package com.app.ChatService.DTO.Response.Chat;

import com.app.ChatService.DTO.BaseDTO.ChatMessageDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {
    String id;
    String user;
    String user2;
    List<ChatMessageDTO> messages;
}
