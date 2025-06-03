package com.app.ChatService.Controller;

import com.app.ChatService.DTO.Request.ChatBot.SendChatBotRequest;
import com.app.ChatService.Entity.ChatBot;
import com.app.ChatService.Service.ChatBotService;
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
    @MessageMapping("/chatbot.send")
    public void send(SendChatBotRequest request, Principal principal) {
        String userName = principal.getName();
        ChatBot response = chatBotService.sendChatBot(userName, request);
        simpMessagingTemplate.convertAndSendToUser(userName,"/queue/message",response);
    }

}
