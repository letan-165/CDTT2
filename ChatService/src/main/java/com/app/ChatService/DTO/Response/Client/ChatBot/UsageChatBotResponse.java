package com.app.ChatService.DTO.Response.Client.ChatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageChatBotResponse {
    Long prompt_tokens;
    Long completion_tokens;
    Long total_tokens;
    String prompt_tokens_details;
}
