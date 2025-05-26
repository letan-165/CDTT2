package com.app.QuizService.DTO.Request;

import com.app.QuizService.DTO.BaseDTO.QuestionEdit;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionEditRequest {
    String quizID;
    List<QuestionEdit> questions;
}
