package com.app.ChatService.DTO.Request.Client;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionEdit {
    Integer questionID;
    String content;
    String type;
    List<String> options;
    List<String> corrects;
}
