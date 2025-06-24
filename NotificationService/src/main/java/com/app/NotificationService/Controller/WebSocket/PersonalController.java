package com.app.NotificationService.Controller.WebSocket;

import com.app.NotificationService.Service.PersonalService;
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
public class PersonalController {
    PersonalService personalService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/personal-ws.list")
    public void list(Principal principal) {
        String name = principal.getName();

        var response = personalService.getNotificationPersonal(name);

        messagingTemplate.convertAndSendToUser(name, "/queue/notification", response);

    }

}
