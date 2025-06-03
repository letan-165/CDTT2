package com.app.ChatService.Config;

import com.app.ChatService.DTO.Request.Client.TokenRequest;
import com.app.ChatService.Repository.HttpClient.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final UserClient userClient;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        URI uri = request.getURI();
        String path = uri.getPath();
        if (path.contains("/chatbot")) {
            attributes.put("connectionType", "chatbot");
        } else {
            attributes.put("connectionType", "chat");
        }

        String query = uri.getQuery();
        String token = null;
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && pair[0].equals("token")) {
                    token = pair[1];
                    break;
                }
            }
        }

        if (token != null) {
            String userName = userClient.findNameFromToken(
                    TokenRequest.builder().token(token).build()
            ).getResult();

            if (userName != null && !userName.isEmpty()) {
                attributes.put("userName", userName);
                return () -> userName;
            }
        }
        return null;
    }

}
