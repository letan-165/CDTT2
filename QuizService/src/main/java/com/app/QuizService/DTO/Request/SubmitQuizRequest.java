package com.app.QuizService.DTO.Request;

import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmitQuizRequest {
    String resultID;
    List<QuestionSubmit> questions;
}
