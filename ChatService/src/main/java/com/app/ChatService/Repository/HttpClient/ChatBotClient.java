package com.app.ChatService.Repository.HttpClient;

import com.app.ChatService.DTO.Request.Client.ChatBotRequest;
import com.app.ChatService.DTO.Response.Client.ChatBot.ChatBotClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "chatbot", url = "${chatbot.url}")
public interface ChatBotClient {
    @PostMapping(value = "/chat/completions" , produces = MediaType.APPLICATION_JSON_VALUE)
    ChatBotClientResponse sendChatBot(@RequestHeader("Authorization")String apiKey, @RequestBody ChatBotRequest request);
}