package com.app.QuizService.DTO.Response.QuizDetail;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {
    int questionID;
    String content;
    List<String> options;
    List<String> corrects;
}
