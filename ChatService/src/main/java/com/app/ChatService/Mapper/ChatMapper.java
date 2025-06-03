package com.app.ChatService.Mapper;

import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.DTO.Response.Client.ChatBot.ChatBotResponse;
import com.app.ChatService.Entity.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatResponse toChatResponse(Chat chat);
}
