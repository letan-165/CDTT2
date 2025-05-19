package com.app.QuizService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionSave {
    String content;
    List<String> options;
    List<Integer> corrects;
}
