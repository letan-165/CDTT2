package com.app.ChatService.DTO.Response.Client.ChatBot;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageChatBotResponse extends MessageDTO {
    String refusal;
    String reasoning;
}
