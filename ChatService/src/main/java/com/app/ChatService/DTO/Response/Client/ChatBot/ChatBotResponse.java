package com.app.ChatService.DTO.Response.Client.ChatBot;

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
public class ChatBotResponse {
    String id;
    String provider;
    String model;
    String object;
    long created;
    List<ChoicesChatBotResponse> choices;
    UsageChatBotResponse usage;
}
