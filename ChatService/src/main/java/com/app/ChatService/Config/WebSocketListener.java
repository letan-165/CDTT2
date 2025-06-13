package com.app.ChatService.Config;

import com.app.ChatService.Service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketListener {
    private final ChatBotService chatBotService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String connectionType = (String) accessor.getSessionAttributes().get("connectionType");
        String userID = (String) accessor.getSessionAttributes().get("userName");

        if (connectionType.equals("chatbot")) {
            chatBotService.deleteChatBot(userID);
        }

    }


}
