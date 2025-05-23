package com.app.QuizService.DTO.Request;

import com.app.QuizService.DTO.BaseDTO.QuestionSave;
import com.app.QuizService.Validation.ValidQuizTime;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidQuizTime
public class SaveQuizRequest {
    String teacherID;
    String title;
    List<String> topics;
    String description;
    List<QuestionSave> questions;
    Instant startTime;
    Instant endTime;
    Duration duration;
}
