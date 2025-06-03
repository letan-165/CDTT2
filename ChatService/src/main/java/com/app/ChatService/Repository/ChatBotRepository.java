package com.app.ChatService.Repository;

import com.app.ChatService.Entity.ChatBot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotRepository extends MongoRepository<ChatBot,String> {
}
