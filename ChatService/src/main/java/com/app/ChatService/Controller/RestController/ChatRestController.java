package com.app.ChatService.Controller.RestController;

import com.app.ChatService.Entity.Chat;
import com.app.ChatService.Service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quizz.library.common.DTO.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatRestController {
    ChatService chatService;

    @GetMapping("/public/{name}")
    ApiResponse<List<Chat>> findAllByUser(@PathVariable String name){
        return ApiResponse.<List<Chat>>builder()
                .message("Lấy danh sách người dùng chat: "+name)
                .result(chatService.findAllByUser(name))
                .build();
    }

    @PostMapping("/{name}/{name2}")
    ApiResponse<Chat> findAllByUser(@PathVariable String name,@PathVariable String name2){
        return ApiResponse.<Chat>builder()
                .result(chatService.createChat(name,name2))
                .build();
    }
}
