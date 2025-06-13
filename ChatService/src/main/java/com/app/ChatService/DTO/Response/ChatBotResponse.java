package com.app.ChatService.DTO.Response;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotResponse {

    String user;
    String type;
    List<MessageDTO> messages;
}
