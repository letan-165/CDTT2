package com.app.ChatService.Service;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import com.app.ChatService.DTO.Request.ChatBot.SendChatBotRequest;
import com.app.ChatService.DTO.Request.Client.ChatBotRequest;
import com.app.ChatService.DTO.Response.Client.ChatBot.ChatBotResponse;
import com.app.ChatService.Entity.ChatBot;
import com.app.ChatService.Enum.ModelChatBot;
import com.app.ChatService.Enum.RoleChatBot;
import com.app.ChatService.Exception.AppException;
import com.app.ChatService.Exception.ErrorCode;
import com.app.ChatService.Mapper.ChatMapper;
import com.app.ChatService.Repository.ChatBotRepository;
import com.app.ChatService.Repository.HttpClient.ChatBotClient;
import com.app.ChatService.Repository.HttpClient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ChatBotService {
    ChatBotRepository chatBotRepository;
    ChatBotClient chatBotClient;
    UserClient userClient;
    ChatMapper chatMapper;

    @NonFinal
    @Value("${chatbot.key}")
    String keyChatBot;

    public ChatBot createChatBot(String name){
        try {
            userClient.findByName(name);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }

        return chatBotRepository.save(ChatBot.builder()
                .user(name)
                .messages(new ArrayList<>())
                .build());
    }

    public ChatBot sendChatBot(String userID,SendChatBotRequest request){
        ChatBot chatBot = chatBotRepository.findById(userID)
                .orElseThrow(()-> new AppException(ErrorCode.CHATBOT_NO_EXISTS));

        chatBot.getMessages().add(MessageDTO.builder()
                        .role(RoleChatBot.USER.getRole())
                        .content(request.getContent())
                .build());

        ChatBotRequest chatBotRequest = ChatBotRequest.builder()
                .model(ModelChatBot.DEEPSEAK_V3.getModel())
                .messages(chatBot.getMessages())
                .build();

        ChatBotResponse chatBotResponse =chatBotClient.sendChatBot("Bearer " + keyChatBot,chatBotRequest);

        String contentChatBotResponse = chatBotResponse.getChoices().get(0).getMessage().getContent();
        chatBot.getMessages().add(MessageDTO.builder()
                        .role(RoleChatBot.ASSISTANT.getRole())
                        .content(contentChatBotResponse)
                .build());

        return chatBotRepository.save(chatBot);
    };

    public void deleteChatBot(String name){
        try {
            userClient.findByName(name);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        chatBotRepository.deleteById(name);
    }



}
