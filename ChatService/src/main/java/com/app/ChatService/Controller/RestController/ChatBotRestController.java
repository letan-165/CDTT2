package com.app.ChatService.Controller.RestController;

import com.app.ChatService.DTO.Request.ChatBot.SendChatBotRequest;
import com.app.ChatService.DTO.Request.Client.QuestionEditRequest;
import com.app.ChatService.DTO.Response.ChatBotResponse;
import com.app.ChatService.Entity.ChatBot;
import com.app.ChatService.Service.ChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quizz.library.common.DTO.ApiResponse;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatBotRestController {
    ChatBotService chatBotService;

    @PostMapping("/{name}")
    public ApiResponse<ChatBotResponse> createChatBot(@PathVariable String name){
        return ApiResponse.<ChatBotResponse>builder()
                .result(chatBotService.createChatBot(name,"QUESTION"))
                .build();
    }

    @PutMapping("/{name}")
    public ApiResponse<ChatBotResponse> sendChatBot(@PathVariable String name, @RequestBody SendChatBotRequest request) throws JsonProcessingException {
        return ApiResponse.<ChatBotResponse>builder()
                .result(chatBotService.sendChatBot(name,request))
                .build();
    }
    @DeleteMapping("/{name}")
    public ApiResponse<Boolean> deleteChatBot(@PathVariable String name){
        return ApiResponse.<Boolean>builder()
                .result(chatBotService.deleteChatBot(name))
                .build();
    }


}
