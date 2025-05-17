package com.app.QuizService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result {
    String quizID;
    int score;
    Instant finish;
    List<Answer> answers;
}
