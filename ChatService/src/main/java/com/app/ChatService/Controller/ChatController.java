package com.app.ChatService.Controller;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import com.app.ChatService.DTO.Request.Chat.CreateChatRequest;
import com.app.ChatService.DTO.Request.Chat.RecallMessageRequest;
import com.app.ChatService.DTO.Request.Chat.SenderMessageRequest;
import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.Service.ChatService;
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
public class ChatController {

    ChatService chatService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(SenderMessageRequest request, Principal principal) {
        String userID = principal.getName();
        ChatResponse chatResponse = chatService.sendChat(userID,request);

        messagingTemplate.convertAndSendToUser(chatResponse.getUser(), "/queue/messages", chatResponse);
        messagingTemplate.convertAndSendToUser(chatResponse.getUser2(), "/queue/messages", chatResponse);
    }


    @MessageMapping("/chat.recall")
    public void recall(RecallMessageRequest request, Principal principal) {
        String userID = principal.getName();

        ChatResponse chatResponse = chatService.recallMessage(request.getId(), userID, request.getIndex());

        messagingTemplate.convertAndSendToUser(chatResponse.getUser(), "/queue/messages", chatResponse);
        messagingTemplate.convertAndSendToUser(chatResponse.getUser2(), "/queue/messages", chatResponse);
    }




}

