package com.app.ChatService.DTO.Response.Client.ChatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChoicesChatBotResponse {
    String logprobs;
    String finish_reason;
    String native_finish_reason;
    Long index;
    MessageChatBotResponse message;
}
