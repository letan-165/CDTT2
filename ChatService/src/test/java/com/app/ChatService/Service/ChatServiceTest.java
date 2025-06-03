package com.app.ChatService.Service;

import com.app.ChatService.DTO.BaseDTO.ChatMessageDTO;
import com.app.ChatService.DTO.Request.Chat.SenderMessageRequest;
import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.Entity.Chat;
import com.app.ChatService.Mapper.ChatMapper;
import com.app.ChatService.Repository.ChatRepository;
import com.app.ChatService.Repository.HttpClient.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Spy
    @InjectMocks ChatService chatService;

    @Mock ChatRepository chatRepository;
    @Mock ChatMapper chatMapper;
    @Mock UserClient userClient;

    Chat chat;
    @Mock ChatResponse chatResponse;

    @BeforeEach
    void initData(){
        chat = Chat.builder()
                .user("user1")
                .user2("user2")
                .messages(new ArrayList<>(List.of(
                        ChatMessageDTO.builder().index(0L).sender("user1").build(),
                        ChatMessageDTO.builder().index(1L).sender("user2").build())))
                .build();
    }

    @Test
    void createChat_success(){
        when(chatRepository.save(any())).thenReturn(chat);
        Chat response = chatService.createChat(chat.getUser(), chat.getUser2());

        assertThat(response).isEqualTo(chat);
    }

    @Test
    void createChat_fail_USER_NO_EXIST(){
        when(userClient.findByName(chat.getUser())).thenThrow(new RuntimeException());
        var exception = assertThrows(AppException.class,
                ()->chatService.createChat(chat.getUser(), chat.getUser2()));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NO_EXIST);
    }

    @Test
    void sendChat_success(){
        SenderMessageRequest request = SenderMessageRequest.builder()
                .receiver(chat.getUser2())
                .content("content")
                .build();
        doReturn(chat).when(chatService).createChat(chat.getUser(),chat.getUser2());
        when(chatRepository.save(any())).thenReturn(chat);
        when(chatMapper.toChatResponse(chat)).thenReturn(chatResponse);

        ChatResponse response = chatService.sendChat(chat.getUser(),request);

        assertThat(response).isEqualTo(chatResponse);
    }

    @Test
    void sendChat_fail_USERID_DUPLICATE(){
        SenderMessageRequest request = SenderMessageRequest.builder()
                .receiver(chat.getUser())
                .content("content")
                .build();

        var exception = assertThrows(AppException.class,
                ()->chatService.sendChat(chat.getUser(),request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USERID_DUPLICATE);
    }

    @Test
    void recallMessage_success(){
        String chatID = "chatID";

        when(chatRepository.findById(chatID)).thenReturn(Optional.of(chat));
        when(chatRepository.save(chat)).thenReturn(chat);
        when(chatMapper.toChatResponse(chat)).thenReturn(chatResponse);

        ChatResponse response = chatService.recallMessage(chatID,chat.getUser(),0);

        assertThat(response).isEqualTo(chatResponse);
    }

    @Test
    void recallMessage_fail_USER_NO_EXISTS_CHAT(){
        String chatID = "chatID";

        when(chatRepository.findById(chatID)).thenReturn(Optional.of(chat));

        var exception=  assertThrows(AppException.class,
                ()->chatService.recallMessage(chatID,"123",0));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NO_EXISTS_CHAT);
    }

    @Test
    void recallMessage_fail_USER_IMPOSSIBLE_RECALL_CHAT(){
        String chatID = "chatID";

        when(chatRepository.findById(chatID)).thenReturn(Optional.of(chat));

        var exception=  assertThrows(AppException.class,
                ()->chatService.recallMessage(chatID,chat.getUser(),1));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_IMPOSSIBLE_RECALL_CHAT);
    }



}
