package com.app.ChatService.Controller;

import com.app.ChatService.DTO.Request.Chat.GetChatRequest;
import com.app.ChatService.DTO.Request.Chat.RecallMessageRequest;
import com.app.ChatService.DTO.Request.Chat.SenderMessageRequest;
import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.Entity.Chat;
import com.app.ChatService.Mapper.ChatMapper;
import com.app.ChatService.Service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatController {

    ChatService chatService;
    SimpMessagingTemplate messagingTemplate;
    ChatMapper chatMapper;

    @MessageMapping("/chat.get")
    public void get(GetChatRequest request, Principal principal) {
        String name = principal.getName();
        Chat chat = chatService.findChatID(name,request.getReceiver());

        ChatResponse chatResponse = (chat !=null )
                ? chatMapper.toChatResponse(chat)
                : ChatResponse.builder()
                .chatID("Chưa khởi tạo")
                .user(name)
                .user2(request.getReceiver())
                .messages(Collections.emptyList())
                .build();

        messagingTemplate.convertAndSendToUser(name, "/queue/messages", chatResponse);
    }

    @MessageMapping("/chat.send")
    public void send(SenderMessageRequest request, Principal principal) {
        String name = principal.getName();
        ChatResponse chatResponse = chatService.sendChat(name,request);

        messagingTemplate.convertAndSendToUser(chatResponse.getUser(), "/queue/messages", chatResponse);
        messagingTemplate.convertAndSendToUser(chatResponse.getUser2(), "/queue/messages", chatResponse);
    }


    @MessageMapping("/chat.recall")
    public void recall(RecallMessageRequest request, Principal principal) {
        String name = principal.getName();

        ChatResponse chatResponse = chatService.recallMessage(request.getChatID(), name, request.getIndex());

        messagingTemplate.convertAndSendToUser(chatResponse.getUser(), "/queue/messages", chatResponse);
        messagingTemplate.convertAndSendToUser(chatResponse.getUser2(), "/queue/messages", chatResponse);
    }




}

