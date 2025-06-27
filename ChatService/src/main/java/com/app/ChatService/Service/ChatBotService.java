package com.app.ChatService.Service;

import com.app.ChatService.DTO.BaseDTO.MessageDTO;
import com.app.ChatService.DTO.Request.ChatBot.SendChatBotRequest;
import com.app.ChatService.DTO.Request.Client.ChatBotRequest;
import com.app.ChatService.DTO.Request.Client.QuestionEditRequest;
import com.app.ChatService.DTO.Response.ChatBotResponse;
import com.app.ChatService.DTO.Response.Client.ChatBot.ChatBotClientResponse;
import com.app.ChatService.DTO.Response.Client.QuizDetail.QuizResponse;
import com.app.ChatService.Entity.ChatBot;
import com.app.ChatService.Enum.ModelChatBot;
import com.app.ChatService.Enum.RoleChatBot;
import com.app.ChatService.Enum.SystemChatBot;
import com.app.ChatService.Mapper.ChatMapper;
import com.app.ChatService.Repository.ChatBotRepository;
import com.app.ChatService.Repository.HttpClient.ChatBotClient;
import com.app.ChatService.Repository.HttpClient.QuizClient;
import com.app.ChatService.Repository.HttpClient.UserClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ChatBotService {
    ChatBotRepository chatBotRepository;
    ChatBotClient chatBotClient;
    UserClient userClient;
    QuizClient quizClient;
    ChatMapper chatMapper;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${chatbot.key}")
    String keyChatBot;


    public ChatBotResponse createChatBot(String name, String type){
        try {
            userClient.findByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
        SystemChatBot systemChatBot = SystemChatBot.valueOf(type);
        MessageDTO messageDTO = MessageDTO.builder()
                .role(RoleChatBot.SYSTEM.getRole())
                .content(systemChatBot.getValue())
                .build();
        return chatMapper.toChatBotResponse(chatBotRepository.save(ChatBot.builder()
                .user(name)
                .type(type)
                .messages(new ArrayList<>(List.of(messageDTO)))
                .build()));
    }

    public ChatBotResponse sendChatBot(String name,SendChatBotRequest request) throws JsonProcessingException {
        ChatBot chatBot = chatBotRepository.findById(name)
                .orElseThrow(()-> new RuntimeException("Chat không tồn tại"));
        if(chatBot.getType().equals("QUESTION")){
            return sendChatBotTypeQuestion(name,request);
        }else{
            return sendChatBotTypeSupport(chatBot,request);
        }
    };

    ChatBotResponse sendChatBotTypeSupport(ChatBot chatBot,SendChatBotRequest request){
        chatBot.getMessages().add(MessageDTO.builder()
                .role(RoleChatBot.USER.getRole())
                .content(request.getContent())
                .build());

        ChatBotRequest chatBotRequest = ChatBotRequest.builder()
                .model(ModelChatBot.DEEPSEAK_V3.getModel())
                .messages(chatBot.getMessages())
                .build();

        ChatBotClientResponse chatBotResponse =chatBotClient.sendChatBot("Bearer " + keyChatBot,chatBotRequest);

        String contentChatBotResponse = chatBotResponse.getChoices().get(0).getMessage().getContent();
        chatBot.getMessages().add(MessageDTO.builder()
                .role(RoleChatBot.ASSISTANT.getRole())
                .content(contentChatBotResponse)
                .build());

        return chatMapper.toChatBotResponse(chatBotRepository.save(chatBot));
    }

    ChatBotResponse sendChatBotTypeQuestion(String name,SendChatBotRequest request) throws JsonProcessingException {
        QuizResponse quizResponse = quizClient.findById(request.getQuizID()).getResult();

        ChatBot chatBot = chatBotRepository.findById(name)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_NO_EXISTS));
        String content = "Thông tin bài quiz: "
                + quizResponse.getTitle()
                + quizResponse.getDescription()
                + Arrays.toString(quizResponse.getTopics().toArray())
                + "Yêu cầu khác hàng: "+ request.getContent();

        chatBot.getMessages().add(MessageDTO.builder()
                .role(RoleChatBot.USER.getRole())
                .content(content)
                .build());
        ChatBotRequest chatBotRequest = ChatBotRequest.builder()
                .model(ModelChatBot.DEEPSEAK_V3.getModel())
                .messages(chatBot.getMessages())
                .build();
        ChatBotClientResponse chatBotResponse = chatBotClient.sendChatBot("Bearer " + keyChatBot,chatBotRequest);

        String contentChatBotResponse = chatBotResponse.getChoices().get(0).getMessage().getContent();
        int start = contentChatBotResponse.indexOf("```json");
        int end = contentChatBotResponse.indexOf("```", start + 7);
        if (start != -1 && end != -1) {
            contentChatBotResponse = contentChatBotResponse.substring(start + 7, end).trim();
        }

        QuestionEditRequest questionEditRequest = objectMapper.readValue(contentChatBotResponse,QuestionEditRequest.class);
        questionEditRequest.setQuizID(request.getQuizID());
        quizClient.saveQuestion(questionEditRequest);
        MessageDTO messageDTO = MessageDTO.builder()
                .role(RoleChatBot.ASSISTANT.getRole())
                .content("Đã lưu các câu hỏi vào bài quiz")
                .build();
        chatBot.getMessages().add(messageDTO);
        return chatMapper.toChatBotResponse(chatBot);
    };

    public Boolean deleteChatBot(String name){
        try {
            userClient.findByName(name);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        chatBotRepository.deleteById(name);
        return true;
    }



}
