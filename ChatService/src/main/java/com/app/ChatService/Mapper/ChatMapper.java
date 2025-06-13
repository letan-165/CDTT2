package com.app.ChatService.Mapper;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.DTO.Response.ChatBotResponse;
import com.app.ChatService.Entity.Chat;
import com.app.ChatService.Entity.ChatBot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatResponse toChatResponse(Chat chat);
    @Mapping(target = "messages", source = "messages", qualifiedByName = "toMessage")
    ChatBotResponse toChatBotResponse(ChatBot chatBot);

    @Named("toMessage")
    default List<MessageDTO> toMessage(List<MessageDTO> list){
        list.remove(0);
        return list;
    }
}
