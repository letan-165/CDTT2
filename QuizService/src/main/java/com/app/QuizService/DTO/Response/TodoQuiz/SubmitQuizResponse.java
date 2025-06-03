package com.app.QuizService.DTO.Response.TodoQuiz;

import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmitQuizResponse {
    String resultID;
    List<QuestionSubmit> questions;
}
