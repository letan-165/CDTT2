package com.app.ChatService.Service;

import com.app.ChatService.DTO.BaseDTO.ChatMessageDTO;
import com.app.ChatService.DTO.Request.Chat.SenderMessageRequest;
import com.app.ChatService.DTO.Response.Chat.ChatResponse;
import com.app.ChatService.Entity.Chat;
import com.app.ChatService.Mapper.ChatMapper;
import com.app.ChatService.Repository.ChatRepository;
import com.app.ChatService.Repository.HttpClient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ChatService {
    ChatRepository chatRepository;
    ChatMapper chatMapper;
    UserClient userClient;


    public List<Chat> findAllByUser(String user1){
        return chatRepository.findAllByUser(user1);
    }


    Chat createChat(String user1,String user2){
        try {
            userClient.findByName(user1);
            userClient.findByName(user2);
        } catch (Exception e) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        return chatRepository.save(Chat.builder()
                .user(user1)
                .user2(user2)
                .messages(new ArrayList<>())
                .build());
    }

    public Chat findChatID(String user1, String user2) {
        if(user1.equals(user2))
            throw new RuntimeException("Trùng id user");

        List<Chat> chats = chatRepository.findAll();
        for(Chat chat : chats){
            if((chat.getUser().equals(user1) && chat.getUser2().equals(user2) ) || ( chat.getUser().equals(user2) && chat.getUser2().equals(user1)))
                return chat;
        }
        return null;
    }


    public ChatResponse sendChat(String sender, SenderMessageRequest request){
        Chat chat = findChatID(sender,request.getReceiver());
        if(chat==null)
            chat = createChat(sender,request.getReceiver());

        long index = chat.getMessages().stream()
                .mapToLong(ChatMessageDTO::getIndex)
                .max()
                .orElse(0);

        chat.getMessages().add(ChatMessageDTO.builder()
                .index(index+1)
                .time(Instant.now())
                .sender(sender)
                .content(request.getContent())
                .build());

        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    public ChatResponse recallMessage(String chatID,String userID, int index){
        Chat chat = chatRepository.findById(chatID)
                .orElseThrow(()->new RuntimeException("Chat không tồn tại"));

        if(!userID.equals(chat.getUser()) && !userID.equals(chat.getUser2()))
            throw new RuntimeException("Người dùng không tồn tại trong chat");

        if(!userID.equals(chat.getMessages().get(index).getSender()))
            throw new RuntimeException("Người dùng không có quyền thu hồi chat");

        chat.getMessages().get(index).setContent("Tin nhắn đã bị thu hồi");
        return chatMapper.toChatResponse(chatRepository.save(chat)) ;
    }



}
