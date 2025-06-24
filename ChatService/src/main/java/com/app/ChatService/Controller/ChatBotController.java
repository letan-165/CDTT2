package com.app.ChatService.Controller;

import com.app.ChatService.DTO.Request.ChatBot.CreateChatBotRequest;
import com.app.ChatService.DTO.Request.ChatBot.SendChatBotRequest;
import com.app.ChatService.DTO.Response.ChatBotResponse;
import com.app.ChatService.Entity.ChatBot;
import com.app.ChatService.Service.ChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatBotController {
    ChatBotService chatBotService;
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chatbot-ws.create")
    public void create(CreateChatBotRequest request, Principal principal) throws JsonProcessingException {
        String name = principal.getName();
        ChatBotResponse response = chatBotService.createChatBot(name, request.getType());
        simpMessagingTemplate.convertAndSendToUser(name,"/queue/message",response);
    }

    @MessageMapping("/chatbot-ws.send")
    public void send(SendChatBotRequest request, Principal principal) throws JsonProcessingException {
        String userName = principal.getName();
        ChatBotResponse response = chatBotService.sendChatBot(userName, request);
        simpMessagingTemplate.convertAndSendToUser(userName,"/queue/message",response);
    }
}
