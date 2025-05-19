package com.app.QuizService.DTO.Response.TodoQuiz;

import com.app.QuizService.Entity.Quiz;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultResponse {
    String resultID;
    Quiz quiz;
    String studentID;
    int score;
    Instant finish;
}
